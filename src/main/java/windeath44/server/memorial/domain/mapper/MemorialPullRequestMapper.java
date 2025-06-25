package windeath44.server.memorial.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import windeath44.server.memorial.domain.dto.response.MemorialCommitResponseDto;
import windeath44.server.memorial.domain.model.MemorialCommit;
import windeath44.server.memorial.domain.model.MemorialPullRequest;
import windeath44.server.memorial.domain.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialPullRequestResponseDto;

@Mapper(componentModel = "spring")
public interface MemorialPullRequestMapper {
  MemorialPullRequestMapper INSTANCE = Mappers.getMapper(MemorialPullRequestMapper.class);

  @Mapping(source = "memorialPullRequestRequestDto.userId", target = "userId")
  MemorialPullRequest toMemorialPullRequest(MemorialPullRequestRequestDto memorialPullRequestRequestDto, MemorialCommit memorialCommit);
  @Mapping(source = "pullRequest.userId", target = "userId")
  @Mapping(source = "memorialCommit", target = "memorialCommit")
  MemorialPullRequestResponseDto toMemorialPullRequestResponseDto(MemorialPullRequest pullRequest, MemorialCommitResponseDto memorialCommit);
}
