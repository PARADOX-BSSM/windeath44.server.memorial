package windeath44.server.memorial.domain.character.mapper;

import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.anime.model.Anime;
import windeath44.server.memorial.domain.character.dto.request.CharacterChangeRequestDto;
import windeath44.server.memorial.domain.character.dto.response.CharacterChangeRequestResponse;
import windeath44.server.memorial.domain.character.model.Character;
import windeath44.server.memorial.domain.character.model.CharacterChangeRequest;
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
                .state(dto.state())
                .imageUrl(dto.imageUrl())
                .bowCount(dto.bowCount())
                .dateOfDeath(dto.dateOfDeath())
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
                request.getState(),
                request.getImageUrl(),
                request.getBowCount(),
                request.getDateOfDeath(),
                request.getSaying()
        );
    }
}
