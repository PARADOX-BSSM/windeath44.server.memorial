package windeath44.server.memorial.domain.mapper;

import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.model.MemorialComment;
import windeath44.server.memorial.domain.model.MemorialCommentLikes;
import windeath44.server.memorial.domain.model.MemorialCommentLikesPrimaryKey;

@Component
public class MemorialCommentLikesMapper {

  public MemorialCommentLikes toMemorialCommentLikes(MemorialCommentLikesPrimaryKey primaryKey) {
    return MemorialCommentLikes.builder()
            .memorialCommentLikesPrimaryKey(primaryKey)
            .build();
  }
}
