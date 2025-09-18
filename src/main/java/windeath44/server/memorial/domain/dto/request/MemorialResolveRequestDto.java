package windeath44.server.memorial.domain.dto.request;

public record MemorialResolveRequestDto(
        Long memorialPullRequestId,
        String resolved
) {
}
