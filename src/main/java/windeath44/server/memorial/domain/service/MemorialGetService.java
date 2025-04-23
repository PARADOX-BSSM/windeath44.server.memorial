package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.entity.repository.MemorialRepository;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialResponseDto;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemorialGetService {
  private final MemorialRepository memorialRepository;

  public List<MemorialResponseDto> findMemorials() {
    List<Object[]> results = memorialRepository.findMemorials();
    for (Object[] row : results) {
      System.out.println(Arrays.toString(row));
    }
    return null;
  }
}
