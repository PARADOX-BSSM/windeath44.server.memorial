package windeath44.server.memorial.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
  @ManyToOne
  @JoinColumn(name="parentId")
  private MemorialComment parentComment;
  private Long likesCount = 0L;
  @CreatedDate
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemorialComment> children = new ArrayList<>();

  public static MemorialComment of(final Memorial memorial, final String userId, final String content, final MemorialComment parentComment) {
    return MemorialComment.builder()
            .memorial(memorial)
            .userId(userId)
            .content(content)
            .parentComment(parentComment)
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

  public Long getParentCommentId() {
    return this.parentComment == null ? null : this.parentComment.getCommentId();
  }

  public void addChild(List<MemorialComment> child) {
    this.children.addAll(child);
  }
}
