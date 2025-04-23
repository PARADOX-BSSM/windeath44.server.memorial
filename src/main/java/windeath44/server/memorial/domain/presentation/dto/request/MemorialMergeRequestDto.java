package windeath44.server.memorial.domain.presentation.dto.request;

public record MemorialMergeRequestDto(
        String userId,
        Long memorialPullRequestId
) {
}
