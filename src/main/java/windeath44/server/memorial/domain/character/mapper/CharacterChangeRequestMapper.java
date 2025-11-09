package windeath44.server.memorial.domain.character.mapper;

import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.anime.model.Anime;
import windeath44.server.memorial.domain.character.dto.request.CharacterChangeRequestDto;
import windeath44.server.memorial.domain.character.dto.response.CharacterChangeRequestResponse;
import windeath44.server.memorial.domain.character.model.Character;
import windeath44.server.memorial.domain.character.model.CharacterChangeRequest;
import windeath44.server.memorial.domain.character.model.type.CauseOfDeath;
import windeath44.server.memorial.domain.character.model.type.CharacterState;
import windeath44.server.memorial.domain.memorial.model.MemorialCommit;

@Component
public class CharacterChangeRequestMapper {

    public CharacterChangeRequest toCharacterChangeRequest(
            CharacterChangeRequestDto dto,
            Character character,
            Anime anime,
            MemorialCommit memorialCommit
    ) {
        return CharacterChangeRequest.builder()
                .character(character)
                .anime(anime)
                .memorialCommit(memorialCommit)
                .name(dto.name())
                .age(dto.age())
                .imageUrl(dto.imageUrl())
                .deathOfDay(dto.deathOfDay())
                .deathReason(CauseOfDeath.valueOfDeathReason(dto.deathReason()))
                .causeOfDeathDetails(dto.causeOfDeathDetails())
                .saying(dto.saying())
                .build();
    }

    public CharacterChangeRequestResponse toCharacterChangeRequestResponse(CharacterChangeRequest request) {
        return new CharacterChangeRequestResponse(
                request.getCharacterApplicationId(),
                request.getCharacter().getCharacterId(),
                request.getAnime().getAnimeId(),
                request.getMemorialCommit().getMemorialCommitId(),
                request.getName(),
                request.getAge(),
                request.getImageUrl(),
                request.getDeathOfDay(),
                request.getDeathReason() != null ? request.getDeathReason().getDeathReason() : null,
                request.getCauseOfDeathDetails(),
                request.getSaying()
        );
    }
}
