package windeath44.server.memorial.domain.dto.request;

public record MemorialCommitRequestDto(
        String userId,
        Long memorialId,
        String content
) {
}
