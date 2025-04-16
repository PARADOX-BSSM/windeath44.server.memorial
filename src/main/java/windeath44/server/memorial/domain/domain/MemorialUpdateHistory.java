package windeath44.server.memorial.domain.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MemorialUpdateHistory {
  @Id @GeneratedValue(GenerationType.IDENTITY)
  private Long memorialUpdateHistoryId;


}
