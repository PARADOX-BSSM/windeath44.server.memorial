package windeath44.server.memorial.domain.memorial.dto.response;

public record MemorialMergeableResponseDto(
        Long memorialPullRequestId,
        Long latestPullRequestId,
        Boolean mergeable,
        String conflict
) {
}
