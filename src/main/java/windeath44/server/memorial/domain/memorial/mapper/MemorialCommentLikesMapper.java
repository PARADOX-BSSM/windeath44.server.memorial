package windeath44.server.memorial.domain.memorial.mapper;

import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.memorial.model.MemorialComment;
import windeath44.server.memorial.domain.memorial.model.MemorialCommentLikes;
import windeath44.server.memorial.domain.memorial.model.MemorialCommentLikesPrimaryKey;

@Component
public class MemorialCommentLikesMapper {

  public MemorialCommentLikes toMemorialCommentLikes(String userId, MemorialComment comment) {
    return MemorialCommentLikes.builder()
            .id(MemorialCommentLikesPrimaryKey.of(comment.getCommentId(), userId))
            .memorialComment(comment)
            .build();
  }
}
