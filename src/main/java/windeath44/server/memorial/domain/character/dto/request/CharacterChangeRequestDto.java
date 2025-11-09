package windeath44.server.memorial.domain.character.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CharacterChangeRequestDto(
        @NotNull(message = "characterId is null")
        Long characterId,
        @NotNull(message = "animeId is null")
        Long animeId,
        @NotNull(message = "memorialCommitId is null")
        Long memorialCommitId,
        @NotNull(message = "name is null")
        String name,
        @NotNull(message = "age is null")
        Integer age,
        String state,
        String imageUrl,
        Long bowCount,
        String dateOfDeath,
        String saying
) {
}
