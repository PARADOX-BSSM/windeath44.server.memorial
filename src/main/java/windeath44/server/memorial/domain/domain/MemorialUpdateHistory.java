package windeath44.server.memorial.domain.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class MemorialUpdateHistory {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memorial_update_history_id;
  private String user_id;

  @ManyToOne
  @JoinColumn(name = "memorial_commit_id")
  private MemorialCommit memorial_commit;

  private LocalDateTime updated_at = LocalDateTime.now();
}
