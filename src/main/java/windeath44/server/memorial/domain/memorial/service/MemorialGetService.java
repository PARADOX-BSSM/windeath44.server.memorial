package windeath44.server.memorial.domain.memorial.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.dto.response.TodayMemorialResponse;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.QMemorialComment;
import windeath44.server.memorial.domain.memorial.model.event.MemorialTracingEvent;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;
import windeath44.server.memorial.domain.memorial.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.memorial.exception.UndefinedOrderByException;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import com.querydsl.jpa.impl.JPAQueryFactory;
import windeath44.server.memorial.global.dto.OffsetPage;

@Service
@RequiredArgsConstructor
public class MemorialGetService {
  private final JPAQueryFactory jpaQueryFactory;
  private final MemorialRepository memorialRepository;

  @Cacheable(cacheNames = "memorial", key = "#memorialId")
  public MemorialResponseDto findMemorialById(Long memorialId) {
    MemorialResponseDto memorial = memorialRepository.findMemorialById(memorialId);

    if (memorial == null) {
      throw new MemorialNotFoundException();
    }

    return memorial;
  }

  @Cacheable(cacheNames = "memorialList", key = "#orderBy + ':' + #page")
  public OffsetPage<MemorialListResponseDto> findMemorials(String orderBy, Long page) {
    validateOrderBy(orderBy);
    OffsetPage<MemorialListResponseDto> memorialListResponseDtoPage = memorialRepository.findMemorialsOrderByAndPage(orderBy, page, 10L);
    if (memorialListResponseDtoPage.values().isEmpty()) {
      throw new MemorialNotFoundException();
    }
    return memorialListResponseDtoPage;
  }
 
  @Cacheable(cacheNames = "memorialListFiltered", key = "#orderBy + ':' + #page + ':' + #characters")
  public OffsetPage<MemorialListResponseDto> findMemorialsFiltered(String orderBy, Long page, List<Long> characters) {
    validateOrderBy(orderBy);
    OffsetPage<MemorialListResponseDto> memorialListResponseDtoPage = memorialRepository.findMemorialsOrderByAndPageCharacterFiltered(orderBy, page, 10L, characters);
    return memorialListResponseDtoPage;
  }

  private void validateOrderBy(String orderBy) {
    if (orderBy==null || orderBy.isEmpty()) {
      throw new UndefinedOrderByException();
    }
    if (!orderBy.equals("recently-updated") && !orderBy.equals("lately-updated") && !orderBy.equals("ascending-bow-count") && !orderBy.equals("descending-bow-count")) {
      throw new UndefinedOrderByException();
    }
  }

  public Memorial findById(Long memorialId) {
    return memorialRepository.findById(memorialId)
            .orElseThrow(MemorialNotFoundException::new);
  }

  @Cacheable(cacheNames = "memorialByIds", key = "#memorialIds")
  public List<MemorialResponseDto> findMemorialByIds(List<Long> memorialIds) {
    return memorialRepository.findByIds(memorialIds);
  }

  public TodayMemorialResponse getTodayMemorial() {
    QMemorialComment memorialComment = QMemorialComment.memorialComment;

    LocalDateTime startOfDay = LocalDate.now(ZoneId.of("Asia/Seoul")).atStartOfDay();
    LocalDateTime endOfDay = LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1).atStartOfDay();

    Tuple topMemorial = jpaQueryFactory
            .select(memorialComment.memorial.memorialId, memorialComment.memorial.characterId)
            .from(memorialComment)
            .where(memorialComment.createdAt.between(startOfDay, endOfDay))
            .groupBy(memorialComment.memorial.memorialId)
            .orderBy(memorialComment.count().desc())
            .fetchFirst();
    Long topMemorialId = topMemorial.get(memorialComment.memorial.memorialId);
    Long topCharacterId = topMemorial.get(memorialComment.memorial.characterId);

    return new TodayMemorialResponse(topMemorialId, topCharacterId);
  }
}
