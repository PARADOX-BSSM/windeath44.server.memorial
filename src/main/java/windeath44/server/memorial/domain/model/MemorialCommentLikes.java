package windeath44.server.memorial.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemorialCommentLikes {
  @EmbeddedId
  private MemorialCommentLikesPrimaryKey memorialCommentLikesPrimaryKey;
  @CreatedDate
  private LocalDateTime likedAt;
}
