package windeath44.server.memorial.domain.model;

public record MemorialCommentLikesCount(
        Long commentId,
        Long likesCount,
        Boolean isLiked
) {
}
