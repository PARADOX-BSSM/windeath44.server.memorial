package windeath44.server.memorial.domain.memorial.repository;

import windeath44.server.memorial.domain.memorial.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialResponseDto;
import windeath44.server.memorial.global.dto.OffsetPage;

import java.util.List;

public interface MemorialRepositoryCustom {
  MemorialResponseDto findMemorialById(Long memorialId);
  OffsetPage<MemorialListResponseDto> findMemorialsOrderByAndPage(String orderBy, Long page, Long pageSize);
  OffsetPage<MemorialListResponseDto> findMemorialsOrderByAndPageCharacterFiltered(String orderBy, Long page, Long pageSize, List<Long> characters);
  List<MemorialResponseDto> findByIds(List<Long> memorialIds);
}