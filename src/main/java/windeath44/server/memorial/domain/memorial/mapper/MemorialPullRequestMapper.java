package windeath44.server.memorial.domain.memorial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialPullRequestResponseDto;

@Mapper(componentModel = "spring")
public interface MemorialPullRequestMapper {
  MemorialPullRequestMapper INSTANCE = Mappers.getMapper(MemorialPullRequestMapper.class);

//  @Mapping(source = "memorialPullRequestRequestDto.userId", target = "userId") 안쓰이는 코드 임시로 주석
//  MemorialPullRequest toMemorialPullRequest(MemorialPullRequestRequestDto memorialPullRequestRequestDto, MemorialCommit memorialCommit);
  MemorialPullRequestResponseDto toMemorialPullRequestResponseDto(MemorialPullRequest pullRequest);
}
