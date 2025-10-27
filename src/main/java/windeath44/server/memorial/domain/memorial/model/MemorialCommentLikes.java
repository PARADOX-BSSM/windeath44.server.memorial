package windeath44.server.memorial.domain.memorial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemorialCommentLikes {

  @EmbeddedId
  private MemorialCommentLikesPrimaryKey id;

  @MapsId("commentId")
  @ManyToOne
  @JoinColumn(name = "comment_id")
  private MemorialComment memorialComment;

  @CreatedDate
  private LocalDateTime likedAt;
}
