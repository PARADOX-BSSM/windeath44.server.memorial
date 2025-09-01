package windeath44.server.memorial.domain.character.service.usecase;

import windeath44.server.memorial.domain.anime.model.Anime;
import windeath44.server.memorial.domain.anime.service.AnimeService;
import windeath44.server.memorial.domain.character.dto.request.CharacterRequest;
import windeath44.server.memorial.domain.character.dto.response.CharacterIdResponse;
import windeath44.server.memorial.domain.character.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCharacterUseCase {
  private final AnimeService animeService;
  private final CharacterService characterService;

  public CharacterIdResponse execute(CharacterRequest characterRequest) {
    Long animeId = characterRequest.animeId();

    Anime anime = animeService.getAnime(animeId);
    CharacterIdResponse characterId = characterService.create(characterRequest, anime);
    return characterId;
  }

}
