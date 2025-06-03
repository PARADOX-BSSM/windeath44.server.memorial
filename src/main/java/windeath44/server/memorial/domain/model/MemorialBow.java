package windeath44.server.memorial.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class MemorialBow {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long bowId;

  public String userId;

  public Long memorialId;

  public Long bowCount;

  public LocalDateTime lastBowedAt = LocalDateTime.now();
}
