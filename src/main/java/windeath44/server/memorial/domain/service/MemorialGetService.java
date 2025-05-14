package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.entity.repository.MemorialRepository;
import windeath44.server.memorial.domain.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.exception.UndefinedOrderByException;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialResponseDto;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemorialGetService {
  private final MemorialRepository memorialRepository;

  public MemorialResponseDto findMemorialById(Long id) {
    MemorialResponseDto memorial = memorialRepository.findMemorialById(id);
    if (memorial==null) {
      throw new MemorialNotFoundException();
    }
    return memorial;
  }

  public List<MemorialListResponseDto> findMemorials(String orderBy, Long page) {
    if (orderBy==null || orderBy.isEmpty()) {
      throw new UndefinedOrderByException();
    }
    if (!orderBy.equals("recently-updated") && !orderBy.equals("lately-updated") && !orderBy.equals("ascending-bow-count") && !orderBy.equals("descending-bow-count")) {
      throw new UndefinedOrderByException();
    }
    List<MemorialListResponseDto> memorialListResponseDtos = memorialRepository.findMemorialsOrderByAndPage(orderBy, page, 10L);
    if (memorialListResponseDtos.isEmpty()) {
      throw new MemorialNotFoundException();
    }
    return memorialListResponseDtos;
  }
}
