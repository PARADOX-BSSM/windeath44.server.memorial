package windeath44.server.memorial.domain.character.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import windeath44.server.memorial.domain.anime.model.Anime;
import windeath44.server.memorial.domain.character.model.type.CauseOfDeath;
import windeath44.server.memorial.domain.character.model.type.CharacterState;
import windeath44.server.memorial.domain.memorial.model.MemorialCommit;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "character_change_request")
public class CharacterChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long characterApplicationId;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private Character character;

    @ManyToOne
    @JoinColumn(name = "anime_id")
    private Anime anime;

    @ManyToOne
    @JoinColumn(name = "memorial_commit_id")
    private MemorialCommit memorialCommit;

    private String name;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private CharacterState state;
    private String imageUrl;
    private String deathOfDay;
    @Enumerated(EnumType.STRING)
    private CauseOfDeath deathReason;
    private String causeOfDeathDetails;
    private String saying;
}
