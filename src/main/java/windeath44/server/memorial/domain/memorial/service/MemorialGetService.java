package windeath44.server.memorial.domain.memorial.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.event.MemorialTracingEvent;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;
import windeath44.server.memorial.domain.memorial.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.memorial.exception.UndefinedOrderByException;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialResponseDto;
import windeath44.server.memorial.global.dto.OffsetPage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemorialGetService {
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
    OffsetPage<MemorialListResponseDto> memorialListResponseDtoPage = memorialRepository.findMemorialsOrderByAndPage(orderBy, page, 10L);
    if (memorialListResponseDtoPage.values().isEmpty()) {
      throw new MemorialNotFoundException();
    }

    return memorialListResponseDtoPage;
  }
 
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

  public List<MemorialResponseDto> findMemorialByIds(List<Long> memorialIds) {
    return memorialRepository.findByIds(memorialIds);
  }
}
