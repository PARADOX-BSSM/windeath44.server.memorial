package windeath44.server.memorial.domain.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MemorialCommit {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "")
  private Long memorialCommitId;

  private Long userId;

  @ManyToOne
  @JoinColumn(name = "memorial_id")
  private Memorial memorial;

  private String content;
  private Long likes;
  private MemorialCommitState state;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Memorial getMemorial() {
    return memorial;
  }

  public void setMemorial(Memorial memorial) {
    this.memorial = memorial;
  }

}
