package windeath44.server.memorial.domain.dto.request;

public record MemorialPullRequestRequestDto(
        String userId,
        Long memorialCommitId
) {
}
