package windeath44.server.memorial.domain.dto.request;

public record MemorialMergeRequestDto(
        String userId,
        Long memorialPullRequestId
) {
}
