package windeath44.server.memorial.domain.memorial.dto.request;


public record MemorialCommentRequestDto (
        String content,
        Long parentCommentId
) {
}
