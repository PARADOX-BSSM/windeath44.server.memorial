package windeath44.server.memorial.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

  @ElementCollection
  private List<String> chiefs;

  private Long bowCount = 0L;

  public Memorial(Long characterId) {
    this.characterId = characterId;
  }
}
