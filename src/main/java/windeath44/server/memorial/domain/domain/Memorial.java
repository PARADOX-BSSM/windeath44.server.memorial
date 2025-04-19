package windeath44.server.memorial.domain.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Memorial {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long memorialId;
  private Long characterId;

  @ElementCollection
  private List<Long> chiefs;

  private Long bowCount = 0L;
}
