package windeath44.server.memorial.domain.memorial.mapper;

import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.memorial.model.MemorialCommentLikes;
import windeath44.server.memorial.domain.memorial.model.MemorialCommentLikesPrimaryKey;

@Component
public class MemorialCommentLikesMapper {

  public MemorialCommentLikes toMemorialCommentLikes(MemorialCommentLikesPrimaryKey primaryKey) {
    return MemorialCommentLikes.builder()
            .memorialCommentLikesPrimaryKey(primaryKey)
            .build();
  }
}
