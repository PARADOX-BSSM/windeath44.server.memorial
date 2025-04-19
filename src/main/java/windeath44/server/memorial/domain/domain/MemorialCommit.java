package windeath44.server.memorial.domain.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MemorialCommit {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memorial_commit_id;

  private String user_id;

  @ManyToOne
  @JoinColumn(name = "memorial_id")
  private Memorial memorial;

  private String content;
  @Enumerated(EnumType.STRING)

  private MemorialCommitState state = MemorialCommitState.PENDING;

  private LocalDateTime created_at = LocalDateTime.now();
}
