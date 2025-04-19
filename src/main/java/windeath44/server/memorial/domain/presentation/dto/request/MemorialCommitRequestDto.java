package windeath44.server.memorial.domain.presentation.dto.request;

public record MemorialCommitRequestDto(
        String userId,
        Long memorialId,
        String content
) {
}
