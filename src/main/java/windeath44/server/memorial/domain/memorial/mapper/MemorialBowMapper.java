package windeath44.server.memorial.domain.memorial.mapper;

import org.mapstruct.Mapper;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialBowResponseDto;
import windeath44.server.memorial.domain.memorial.model.MemorialBow;

@Mapper(componentModel = "spring")
public interface MemorialBowMapper {
  MemorialBowResponseDto toMemorialBowResponseDto(MemorialBow bow);

  default MemorialBowResponseDto toMemorialBowResponseDto(MemorialBow bow, Long curruntBowRanking) {
    if (bow == null) {
      return null;
    }
    return new MemorialBowResponseDto(
        bow.getBowId(),
        bow.getMemorialId(),
        bow.getUserId(),
        bow.getBowCount(),
        curruntBowRanking,
        bow.getLastBowedAt());
  }
}
