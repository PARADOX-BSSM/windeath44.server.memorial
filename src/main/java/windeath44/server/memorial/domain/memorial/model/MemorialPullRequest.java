package windeath44.server.memorial.domain.memorial.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import windeath44.server.memorial.domain.character.dto.request.CharacterRequest;
import windeath44.server.memorial.domain.character.model.CharacterChangeRequest;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class MemorialPullRequest {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memorialPullRequestId;
  private String userId = null;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "from_id")
  private MemorialCommit fromCommit;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "to_id")
  private MemorialCommit toCommit;

  @ManyToOne
  @JoinColumn(name = "memorial_id")
  private Memorial memorial;

  @Enumerated(EnumType.STRING)
  private MemorialPullRequestState state = MemorialPullRequestState.PENDING;

  @UpdateTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public MemorialPullRequest(MemorialCommit fromCommit, MemorialCommit toCommit, Memorial memorial, String userId) {
    this.fromCommit = fromCommit;
    this.toCommit = toCommit;
    this.memorial = memorial;
    this.userId = userId;
  }

  public MemorialPullRequest(MemorialCommit fromCommit, MemorialCommit toCommit, Memorial memorial, String userId, MemorialPullRequestState state) {
    this.fromCommit = fromCommit;
    this.toCommit = toCommit;
    this.memorial = memorial;
    this.userId = userId;
    this.state = state;
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

  public boolean isChangedCharacter() {
    return null != this.toCommit.getCharacterChangeRequest();
  }

  public Long getCharacterId() {
    return this.memorial.getCharacterId();
  }

  public CharacterChangeRequest getCharacterUpdateRequest() {
    return this.toCommit.getCharacterChangeRequest();
  }
}