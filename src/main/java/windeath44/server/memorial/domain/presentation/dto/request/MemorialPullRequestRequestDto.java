package windeath44.server.memorial.domain.presentation.dto.request;

public record MemorialPullRequestRequestDto(
        String userId,
        Long memorialCommitId
) {
}
