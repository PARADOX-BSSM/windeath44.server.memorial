package windeath44.server.memorial.domain.character.service;

import windeath44.server.memorial.domain.anime.model.Anime;
import windeath44.server.memorial.domain.character.dto.request.CharacterRequest;
import windeath44.server.memorial.domain.character.dto.response.CharacterIdResponse;
import windeath44.server.memorial.domain.character.exception.NotFoundCharacterException;
import windeath44.server.memorial.domain.character.mapper.CharacterMapper;
import windeath44.server.memorial.domain.character.model.Character;
import windeath44.server.memorial.domain.character.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterCommandService {
  private final CharacterRepository characterRepository;
  private final CharacterMapper characterMapper;

  public CharacterIdResponse create(CharacterRequest characterRequest, Anime anime) {
    Character character = characterMapper.toCharacter(characterRequest, anime);
    Character savedCharacter = characterRepository.save(character);
    CharacterIdResponse characterIdsResponse = characterMapper.toCharacterIdResponse(savedCharacter);
    return characterIdsResponse;
  }

  public void memorializing(Long characterId) {
    Character character = findCharacterById(characterId);
    character.memorializing();
  }

  public void deleteById(Long characterId) {
    Character character = findCharacterById(characterId);
    characterRepository.delete(character);
  }

  public void update(CharacterRequest characterUpdateRequest, Long characterId) {
    Character character = findCharacterById(characterId);
    character.update(characterUpdateRequest);
  }

  public void updateImage(Long characterId, String imageUrl) {
    Character character = findCharacterById(characterId);
    character.updateImage(imageUrl);
  }

  public Character findById(Long characterId) {
    return findCharacterById(characterId);
  }

  private Character findCharacterById(Long characterId) {
    return characterRepository.findById(characterId)
            .orElseThrow(NotFoundCharacterException::getInstance);
  }
}
