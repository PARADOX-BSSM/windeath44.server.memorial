package windeath44.server.memorial.domain.character.dto.response;

import java.time.LocalDateTime;

public record CharacterChangeRequestResponse(
        Long characterApplicationId,
        Long characterId,
        Long animeId,
        Long memorialCommitId,
        String name,
        Integer age,
        String imageUrl,
        String deathOfDay,
        String deathReason,
        String causeOfDeathDetails,
        String saying
) {
}
