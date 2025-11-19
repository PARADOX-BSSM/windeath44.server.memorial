package windeath44.server.memorial.domain.memorial.service;

import lombok.RequiredArgsConstructor;
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
  private final ApplicationEventPublisher applicationEventPublisher;

  public MemorialResponseDto findMemorialById(Long memorialId, String userId) {
    MemorialResponseDto memorial = memorialRepository.findMemorialById(memorialId);

    if (memorial==null) {
      throw new MemorialNotFoundException();
    }

    if (userId != null) applicationEventPublisher.publishEvent(new MemorialTracingEvent(memorialId, userId));

    return memorial;
  }

  public OffsetPage<MemorialListResponseDto> findMemorials(String orderBy, Long page) {
    validateOrderBy(orderBy);
    OffsetPage<MemorialListResponseDto> memorialListResponseDtoList = memorialRepository.findMemorialsOrderByAndPage(orderBy, page, 10L);
    if (memorialListResponseDtoList.values().isEmpty()) {
      throw new MemorialNotFoundException();
    }
    return memorialListResponseDtoList;
  }
 
  public OffsetPage<MemorialListResponseDto> findMemorialsFiltered(String orderBy, Long page, List<Long> characters) {
    validateOrderBy(orderBy);
    OffsetPage<MemorialListResponseDto> memorialListResponseDtoList = memorialRepository.findMemorialsOrderByAndPageCharacterFiltered(orderBy, page, 10L, characters);
    return memorialListResponseDtoList;
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

  public TodayMemorialResponse getTodayMemorial() {
    QMemorialComment memorialComment = QMemorialComment.memorialComment;

    LocalDateTime startOfDay = LocalDate.now(ZoneId.of("Asia/Seoul")).atStartOfDay();
    LocalDateTime endOfDay = LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1).atStartOfDay();

    Long topMemorialId = jpaQueryFactory
            .select(memorialComment.memorial.memorialId)
            .from(memorialComment)
            .where(memorialComment.createdAt.between(startOfDay, endOfDay))
            .groupBy(memorialComment.memorial.memorialId)
            .orderBy(memorialComment.count().desc())
            .fetchFirst();

    return new TodayMemorialResponse(topMemorialId);
  }
}
