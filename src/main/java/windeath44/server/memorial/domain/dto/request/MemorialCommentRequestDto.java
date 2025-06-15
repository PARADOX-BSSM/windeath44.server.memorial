package windeath44.server.memorial.domain.dto.request;

public record MemorialCommentRequestDto (
        String content,
        Long parentCommentId
) {
}
