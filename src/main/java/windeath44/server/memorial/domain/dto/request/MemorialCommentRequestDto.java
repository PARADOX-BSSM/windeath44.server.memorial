package windeath44.server.memorial.domain.dto.request;

public record MemorialCommentRequestDto (
        Long memorialId,
        String content,
        Long parentCommentId
) {
}
