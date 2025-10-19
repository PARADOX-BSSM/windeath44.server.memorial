package windeath44.server.memorial.domain.character.model;


import windeath44.server.memorial.domain.anime.model.Anime;
import windeath44.server.memorial.domain.character.dto.request.CharacterRequest;
import windeath44.server.memorial.domain.character.model.type.CauseOfDeath;
import windeath44.server.memorial.domain.character.model.type.CharacterState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name="`character`")
public class Character {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long characterId;

  @ManyToOne
  @JoinColumn(name="anime_id")
  private Anime anime;
  private String name;
  private Integer age;
  @Enumerated(EnumType.STRING)
  private CauseOfDeath deathReason;
  private String causeOfDeathDetails;
  private String imageUrl;
  private String saying;
  @Enumerated(EnumType.STRING)
  private CharacterState state;
  private Long bowCount;
  private String deathOfDay;

  @PrePersist
  public void init() {
    this.bowCount = 0L;
    this.state = CharacterState.NOT_MEMORIALIZING;
  }

  public String getDeathReason() {
    return deathReason.getDeathReason();
  }

  public void memorializing() {
    this.state = CharacterState.MEMORIALIZING;
  }

  public void update(CharacterRequest characterUpdateRequest) {
    String name = characterUpdateRequest.name();
    CauseOfDeath deathReason = CauseOfDeath.valueOfDeathReason(characterUpdateRequest.deathReason());
    Integer age = characterUpdateRequest.age();
    String deathOfDay = characterUpdateRequest.deathOfDay();
    String saying = characterUpdateRequest.saying();
    String causeOfDeathDetails = characterUpdateRequest.causeOfDeathDetails();

    this.name = name;
    this.age = age;
    this.deathReason = deathReason;
    this.deathOfDay = deathOfDay;
    this.saying = saying;
    this.causeOfDeathDetails = causeOfDeathDetails;
  }

  public void updateImage(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Long getAnimeId() {
    return this.anime.getAnimeId();
  }
}
