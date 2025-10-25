package windeath44.server.memorial.domain.character.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CharacterRequest (
        @NotNull(message="animeId is null")
        Long animeId,
        @NotNull(message="name is null")
        String name,
        @NotNull(message="age is null")
        Integer age,
        @NotNull(message="saying is null")
        @NotEmpty(message="saying is null")
        String saying,
        @NotNull(message="deathReason is null")
        String deathReason,
        String causeOfDeathDetails,
        String deathOfDay,
        @NotNull(message="character image is null")
        String imageUrl
) {

}