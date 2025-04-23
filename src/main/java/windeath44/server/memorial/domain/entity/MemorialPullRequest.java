package windeath44.server.memorial.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class MemorialPullRequest {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memorialPullRequestId;
  private String userId = null;

  @ManyToOne
  @JoinColumn(name = "memorial_commit_id")
  private MemorialCommit memorialCommit;

  @ManyToOne
  @JoinColumn(name = "memorial_id")
  private Memorial memorial;

  @Enumerated(EnumType.STRING)
  private MemorialPullRequestState state = MemorialPullRequestState.PENDING;

  private LocalDateTime updatedAt = LocalDateTime.now();

  public MemorialPullRequest(MemorialCommit memorialCommit, Memorial memorial, String userId) {
    this.memorialCommit = memorialCommit;
    this.memorial = memorial;
    this.userId = userId;
  }

  public void approve() {
    this.state = MemorialPullRequestState.APPROVED;
  }

  public void reject() {
    this.state = MemorialPullRequestState.REJECTED;
  }

  public void store() {
    this.state = MemorialPullRequestState.STORED;
  }

  public void merger(String userId) {
    this.userId = userId;
  }

  public Boolean isAlreadyApproved() {
    return this.state == MemorialPullRequestState.APPROVED;
  }

}
