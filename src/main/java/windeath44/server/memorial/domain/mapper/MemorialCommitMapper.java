package windeath44.server.memorial.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.MemorialCommit;
import windeath44.server.memorial.domain.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialCommitResponseDto;

@Mapper(componentModel = "spring")
public interface MemorialCommitMapper {
  MemorialCommitMapper INSTANCE = Mappers.getMapper(MemorialCommitMapper.class);

  MemorialCommit toMemorialCommit(MemorialCommitRequestDto memorialCommitRequestDto, Memorial memorial);
  MemorialCommitResponseDto toMemorialCommitResponseDto(MemorialCommit memorialCommit);
}
