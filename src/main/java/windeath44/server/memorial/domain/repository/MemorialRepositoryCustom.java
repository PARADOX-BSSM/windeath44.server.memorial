package windeath44.server.memorial.domain.repository;

import windeath44.server.memorial.domain.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.dto.response.MemorialResponseDto;

import java.util.List;

public interface MemorialRepositoryCustom {
  MemorialResponseDto findMemorialById(Long memorialId);
  List<MemorialListResponseDto> findMemorialsOrderByAndPage(String orderBy, Long page, Long pageSize);
  List<MemorialListResponseDto> findMemorialsOrderByAndPageCharacterFiltered(String orderBy, Long page, Long pageSize, List<Long> characters);
}