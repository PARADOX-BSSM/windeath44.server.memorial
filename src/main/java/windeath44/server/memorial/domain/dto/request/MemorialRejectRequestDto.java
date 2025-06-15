package windeath44.server.memorial.domain.dto.request;

public record MemorialRejectRequestDto(
        String userId,
        Long memorialPullRequestId
) {
}
