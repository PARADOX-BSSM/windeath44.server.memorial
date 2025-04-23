package windeath44.server.memorial.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import windeath44.server.memorial.domain.entity.MemorialCommit;
import windeath44.server.memorial.domain.entity.MemorialPullRequest;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestDto;

@Mapper(componentModel = "spring")
public interface MemorialPullRequestMapper {
  MemorialPullRequestMapper INSTANCE = Mappers.getMapper(MemorialPullRequestMapper.class);

  @Mapping(source = "memorialPullRequestDto.userId", target = "userId")
  MemorialPullRequest toMemorialPullRequest(MemorialPullRequestDto memorialPullRequestDto, MemorialCommit memorialCommit);
}
