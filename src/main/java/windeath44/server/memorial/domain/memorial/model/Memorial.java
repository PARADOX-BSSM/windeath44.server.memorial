package windeath44.server.memorial.domain.memorial.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

  private String creatorId;

  @OneToMany(mappedBy = "memorial", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemorialComment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "memorial", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemorialChiefs> chiefs = new ArrayList<>();

  @OneToMany(mappedBy = "memorial", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<MemorialCommit> commits = new ArrayList<>();

  @OneToMany(mappedBy = "memorial", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<MemorialPullRequest> pullRequests = new ArrayList<>();

  public Memorial(Long characterId) {
    this.characterId = characterId;
  }

  public Memorial(Long characterId, String creatorId) {
    this.characterId = characterId;
    this.creatorId = creatorId;
  }

  public void plusBowCount() {
    bowCount++;
  }

}
