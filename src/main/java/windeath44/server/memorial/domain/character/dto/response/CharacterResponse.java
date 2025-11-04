package windeath44.server.memorial.domain.character.dto.response;

import lombok.Builder;

@Builder
public record CharacterResponse (
        Long characterId,
        Long animeId,
        String name,
        Integer age,
        String deathReason,
        String causeOfDeathDetails,
        String imageUrl,
        String deathOfDay,
        Long bowCount,
        String state,
        String saying
) {

}