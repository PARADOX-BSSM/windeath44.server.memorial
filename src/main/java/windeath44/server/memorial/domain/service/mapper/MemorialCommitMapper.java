package windeath44.server.memorial.domain.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import windeath44.server.memorial.domain.domain.MemorialCommit;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;

@Mapper
public interface MemorialCommitMapper {
  MemorialCommitMapper INSTANCE = Mappers.getMapper(MemorialCommitMapper.class);

  MemorialCommit toMemorialCommit(MemorialCommitRequestDto requestDto);
}
