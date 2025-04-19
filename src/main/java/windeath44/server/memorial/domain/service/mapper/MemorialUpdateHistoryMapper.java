package windeath44.server.memorial.domain.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import windeath44.server.memorial.domain.domain.Memorial;
import windeath44.server.memorial.domain.domain.MemorialCommit;
import windeath44.server.memorial.domain.domain.MemorialUpdateHistory;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialUpdateHistoryRequestDto;

@Mapper(componentModel = "spring")
public interface MemorialUpdateHistoryMapper {
  MemorialUpdateHistoryMapper INSTANCE = Mappers.getMapper(MemorialUpdateHistoryMapper.class);

  @Mapping(source = "memorialUpdateHistoryRequestDto.userId", target = "userId")
  MemorialUpdateHistory toMemorialUpdateHistory(MemorialUpdateHistoryRequestDto memorialUpdateHistoryRequestDto, MemorialCommit memorial_commit);
}
