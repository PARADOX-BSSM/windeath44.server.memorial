package windeath44.server.memorial.domain.memorial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
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
  @Builder.Default
  private Long likesCount = 0L;
  @CreatedDate
  private LocalDateTime createdAt;

  @Builder.Default
  @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
  @BatchSize(size = 100)
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

  public void upLikes() {
    this.likesCount++;
  }

  public void downLikes() {
    if (likesCount > 0) this.likesCount--;
  }

  public void deleteByParents() {
    if (parentComment == null) return;
    this.parentComment.removeChild(this);
  }

  private void removeChild(MemorialComment memorialComment) {
    this.children.remove(memorialComment);
  }
}
