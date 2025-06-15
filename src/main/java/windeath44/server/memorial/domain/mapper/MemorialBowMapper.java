package windeath44.server.memorial.domain.mapper;

import org.mapstruct.Mapper;
import windeath44.server.memorial.domain.dto.response.MemorialBowResponseDto;
import windeath44.server.memorial.domain.model.MemorialBow;

@Mapper(componentModel = "spring")
public interface MemorialBowMapper {
  MemorialBowResponseDto toMemorialBowResponseDto(MemorialBow bow);
}
