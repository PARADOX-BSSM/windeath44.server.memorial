package windeath44.server.memorial.domain.domain;

import jakarta.persistence.*;
import lombok.*;

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
  private String content;
  private LocalDateTime createdAt = LocalDateTime.now();

}
