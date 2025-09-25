package windeath44.server.memorial.domain.memorial.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class MemorialCommentLikesPrimaryKey {
  @ManyToOne
  @JoinColumn(name="commentId")
  private MemorialComment comment;
  private String userId;

  public static MemorialCommentLikesPrimaryKey of(MemorialComment comment, String userId) {
    return new MemorialCommentLikesPrimaryKey(comment, userId);
  }
}
