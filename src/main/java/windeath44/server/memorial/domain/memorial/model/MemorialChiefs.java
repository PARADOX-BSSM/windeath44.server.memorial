package windeath44.server.memorial.domain.memorial.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class MemorialChiefs {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userId;

  @ManyToOne
  private Memorial memorial;

  public MemorialChiefs(Memorial memorial, String userId) {
    this.memorial = memorial;
    this.userId = userId;
  }
}