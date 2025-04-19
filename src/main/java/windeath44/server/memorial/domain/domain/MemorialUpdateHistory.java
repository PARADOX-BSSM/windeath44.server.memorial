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
  private Long memorialUpdateHistoryId;
  private String userId;

  @ManyToOne
  @JoinColumn(name = "memorial_commit_id")
  private MemorialCommit memorialCommit;

  private LocalDateTime updatedAt = LocalDateTime.now();
}
