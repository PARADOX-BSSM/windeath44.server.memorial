package windeath44.server.memorial.domain.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Memorial {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long memorial_id;
  private Long character_id;

  @ElementCollection
  private List<Long> chiefs;

  private Long bow_count = 0L;
}
