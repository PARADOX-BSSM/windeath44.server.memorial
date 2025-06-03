package windeath44.server.memorial.domain.dto.request;

public record MemorialResolveRequestDto(
        String userId,
        Long memorialPullRequestId,
        String resolved
) {
}
