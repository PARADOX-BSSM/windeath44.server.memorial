package windeath44.server.memorial.domain.entity.repository;

import windeath44.server.memorial.domain.presentation.dto.response.MemorialResponseDto;

public interface MemorialRepositoryCustom {
  MemorialResponseDto findMemorialById(Long memorialId);
}