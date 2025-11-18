package windeath44.server.memorial.domain.memorial.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class MemorialBow {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long bowId;

  public String userId;

  public Long memorialId;

  public Long bowCount;

  public LocalDateTime lastBowedAt = LocalDateTime.now();

  public MemorialBow(String userId, Long memorialId) {
    this.userId = userId;
    this.memorialId = memorialId;
    this.bowCount = 1L;
  }

  public void plusBowCount() {
    this.bowCount++;
  }

  public void updateLastBowedAt(LocalDateTime lastBowedAt) {
    this.lastBowedAt = lastBowedAt;
  }
}
