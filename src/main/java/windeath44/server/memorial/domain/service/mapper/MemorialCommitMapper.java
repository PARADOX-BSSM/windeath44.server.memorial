package windeath44.server.memorial.domain.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import windeath44.server.memorial.domain.domain.Memorial;
import windeath44.server.memorial.domain.domain.MemorialCommit;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialResponseDto;

@Mapper(componentModel = "spring")
public interface MemorialCommitMapper {
  MemorialCommitMapper INSTANCE = Mappers.getMapper(MemorialCommitMapper.class);

//  @Mapping(source = "memorialCommitRequestDto.user_id", target = "user_id")
//  @Mapping(source = "memorialCommitRequestDto.content", target = "content")
//  @Mapping(source = "memorial", target = )
  MemorialCommit toMemorialCommit(MemorialCommitRequestDto memorialCommitRequestDto, Memorial memorial);
}
