package windeath44.server.memorial.domain.character.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CharacterRequest (
        @NotNull(message="animeId is null")
        Long animeId,
        @NotNull(message="name is null")
        String name,
        @NotNull(message="age is null")
        Integer age,
        @NotNull(message="lifeTime is null")
        Long lifeTime,
        @NotNull(message="saying is null")
        @NotEmpty(message="saying is null")
        String saying,
        @NotNull(message="deathReason is null")
        String deathReason,
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul", shape = JsonFormat.Shape.STRING)
        LocalDate deathOfDay
) {

}