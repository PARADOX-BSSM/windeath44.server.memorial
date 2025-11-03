package windeath44.server.memorial.domain.memorial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;
import windeath44.server.memorial.domain.memorial.model.MemorialChiefs;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialPullRequestResponseDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommitResponseDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialResponseDto;

@Mapper(componentModel = "spring", uses = {MemorialCommitMapper.class, MemorialMapper.class})
public interface MemorialPullRequestMapper {
  MemorialPullRequestMapper INSTANCE = Mappers.getMapper(MemorialPullRequestMapper.class);

//  @Mapping(source = "memorialPullRequestRequestDto.userId", target = "userId") 안쓰이는 코드 임시로 주석
//  MemorialPullRequest toMemorialPullRequest(MemorialPullRequestRequestDto memorialPullRequestRequestDto, MemorialCommit memorialCommit);
  
  default MemorialPullRequestResponseDto toMemorialPullRequestResponseDto(MemorialPullRequest pullRequest) {
    if (pullRequest == null) return null;
    
    MemorialCommitResponseDto memorialCommitDto = MemorialCommitMapper.INSTANCE
        .toMemorialCommitResponseDto(pullRequest.getToCommit());
    
    MemorialResponseDto memorialDto = MemorialMapper.INSTANCE.toMemorialResponseDto(
        pullRequest.getMemorial(),
        pullRequest.getMemorial().getChiefs().stream()
            .map(MemorialChiefs::getUserId)
            .toList(),
        pullRequest.getUserId(),
        pullRequest.getUpdatedAt()
    );
    
    return new MemorialPullRequestResponseDto(
        pullRequest.getMemorialPullRequestId(),
        pullRequest.getUserId(),
        memorialCommitDto,
        memorialDto,
        pullRequest.getState(),
        pullRequest.getUpdatedAt()
    );
  }
}
