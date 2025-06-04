package windeath44.server.memorial.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
public class MemorialComment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;
  @ManyToOne
  @JoinColumn(name="memorialId")
  private Memorial memorial;
  private String userId;
  private String content;
  private Long parentId;
  @CreatedDate
  private LocalDateTime createdAt;

  public static MemorialComment of(final Memorial memorial, final String userId, final String content, final Long parentCommentId) {
    return MemorialComment.builder()
            .memorial(memorial)
            .userId(userId)
            .content(content)
            .parentId(parentCommentId)
            .build();
  }

  public Long getMemorialId() {
    return this.memorial.getMemorialId();
  }

  public void rewrite(String content) {
    this.content = content;
  }

  public MemorialCommentLikesPrimaryKey likesKey(String userId) {
    return MemorialCommentLikesPrimaryKey.of(this, userId);
  }
}
