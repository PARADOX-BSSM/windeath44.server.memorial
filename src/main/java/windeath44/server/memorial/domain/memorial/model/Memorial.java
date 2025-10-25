package windeath44.server.memorial.domain.memorial.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Memorial {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long memorialId;
  private Long characterId;

  private Long bowCount = 0L;

  public Memorial(Long characterId) {
    this.characterId = characterId;
  }

  public void plusBowCount() {
    bowCount++;
  }

}
