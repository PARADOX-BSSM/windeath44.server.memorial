package windeath44.server.memorial.domain.presentation.dto.request;

public record MemorialRejectRequestDto(
        String userId,
        Long memorialPullRequestId
) {
}
