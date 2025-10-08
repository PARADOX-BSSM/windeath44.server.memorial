package windeath44.server.memorial.domain.memorial.repository.projection;

import windeath44.server.memorial.domain.memorial.model.MemorialComment;

public interface MemorialCommentWithLikeProjection {
    MemorialComment getComment();
    Boolean getIsLiked();
}