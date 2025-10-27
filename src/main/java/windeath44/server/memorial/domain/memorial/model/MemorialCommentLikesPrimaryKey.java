package windeath44.server.memorial.domain.memorial.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class MemorialCommentLikesPrimaryKey implements Serializable {
  private Long commentId;
  private String userId;

  public static MemorialCommentLikesPrimaryKey of(Long commentId, String userId) {
    return new MemorialCommentLikesPrimaryKey(commentId, userId);
  }
}
