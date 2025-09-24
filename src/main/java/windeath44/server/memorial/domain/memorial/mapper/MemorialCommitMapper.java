package windeath44.server.memorial.domain.memorial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialCommit;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommitResponseDto;

@Mapper(componentModel = "spring")
public interface MemorialCommitMapper {
  MemorialCommitMapper INSTANCE = Mappers.getMapper(MemorialCommitMapper.class);

  MemorialCommit toMemorialCommit(String userId, MemorialCommitRequestDto memorialCommitRequestDto, Memorial memorial);
  MemorialCommitResponseDto toMemorialCommitResponseDto(MemorialCommit memorialCommit);
  MemorialCommitResponseDto toMemorialCommitResponseDto(MemorialCommit memorialCommit, Memorial memorial);
}
