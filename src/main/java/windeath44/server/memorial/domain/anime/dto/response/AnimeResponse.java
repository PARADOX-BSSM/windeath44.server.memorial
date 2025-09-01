package windeath44.server.memorial.domain.anime.dto.response;

import java.util.List;

public record AnimeResponse (
        Long animeId,
        String name,
        List<String> genres,
        String imageUrl
) {
}
