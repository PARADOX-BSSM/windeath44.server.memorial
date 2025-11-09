package windeath44.server.memorial.domain.memorial.model;

import jakarta.persistence.*;
import lombok.*;
import windeath44.server.memorial.domain.character.model.CharacterChangeRequest;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemorialCommit {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memorialCommitId;
  private String userId;
  @ManyToOne
  @JoinColumn(name = "memorial_id")
  private Memorial memorial;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;
  private LocalDateTime createdAt = LocalDateTime.now();

  @OneToOne(mappedBy = "memorialCommit")
  private CharacterChangeRequest characterChangeRequest;

  public MemorialCommit(String userId, Memorial memorial, String content) {
    this.userId = userId;
    this.memorial = memorial;
    this.content = content;
  }
}
