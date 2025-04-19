package windeath44.server.memorial.domain.presentation.dto.request;

public record MemorialCommitRequestDto(
        String user_id,
        Long memorial_id,
        String content
) {
}
