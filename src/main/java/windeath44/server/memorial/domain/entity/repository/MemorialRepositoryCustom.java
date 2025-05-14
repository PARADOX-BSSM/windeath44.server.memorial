package windeath44.server.memorial.domain.entity.repository;

import windeath44.server.memorial.domain.presentation.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialResponseDto;

import java.util.List;

public interface MemorialRepositoryCustom {
  MemorialResponseDto findMemorialById(Long memorialId);
  List<MemorialListResponseDto> findMemorialsOrderByAndPage(String orderBy, Long page, Long pageSize);
  List<MemorialListResponseDto> findMemorialsOrderByAndPageCharacterFiltered(String orderBy, Long page, Long pageSize, List<Long> characters);
}