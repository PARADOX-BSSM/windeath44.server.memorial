package windeath44.server.memorial.domain.anime.model;

import windeath44.server.memorial.domain.character.model.Character;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Anime {
  @Id
  private Long animeId;
  private String name;

  @ElementCollection
  @Fetch(FetchMode.SUBSELECT)
  private List<String> genres;

  private String imageUrl;

  @OneToMany(mappedBy="anime", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Character> characterList;

}
