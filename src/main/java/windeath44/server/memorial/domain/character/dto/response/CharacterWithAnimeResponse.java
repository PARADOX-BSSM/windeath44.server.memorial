package windeath44.server.memorial.domain.character.dto.response;

import lombok.Builder;
import windeath44.server.memorial.domain.anime.dto.response.AnimeResponse;

@Builder
public record CharacterWithAnimeResponse(
        CharacterResponse character,
        AnimeResponse anime
) {
}
