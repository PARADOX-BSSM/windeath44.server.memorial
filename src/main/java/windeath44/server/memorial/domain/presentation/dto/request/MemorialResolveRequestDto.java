package windeath44.server.memorial.domain.presentation.dto.request;

public record MemorialResolveRequestDto(
        String userId,
        Long memorialPullRequestId,
        String resolved
) {
}
