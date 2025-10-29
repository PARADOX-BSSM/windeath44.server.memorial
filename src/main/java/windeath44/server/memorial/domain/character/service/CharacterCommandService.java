package windeath44.server.memorial.domain.character.service;

import windeath44.server.memorial.domain.anime.model.Anime;
import windeath44.server.memorial.domain.character.dto.request.CharacterRequest;
import windeath44.server.memorial.domain.character.dto.response.CharacterIdResponse;
import windeath44.server.memorial.domain.character.exception.NotFoundCharacterException;
import windeath44.server.memorial.domain.character.mapper.CharacterMapper;
import windeath44.server.memorial.domain.character.model.Character;
import windeath44.server.memorial.domain.character.repository.CharacterRepository;
import windeath44.server.memorial.global.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterCommandService {
  private final CharacterRepository characterRepository;
  private final CharacterMapper characterMapper;
  private final FileStorage fileStorage;

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
    String oldImageUrl = character.getImageUrl();
    String newImageUrl = characterUpdateRequest.imageUrl();
    
    character.update(characterUpdateRequest);
    boolean equalsImageUrl = newImageUrl.equals(oldImageUrl);
    if (equalsImageUrl || oldImageUrl == null) return;
    // image가 다를 시, 기존 이미지 버킷에서 삭제
    deleteImageFromStorage(oldImageUrl);
  }


  public Character findById(Long characterId) {
    return findCharacterById(characterId);
  }

  private Character findCharacterById(Long characterId) {
    return characterRepository.findById(characterId)
            .orElseThrow(NotFoundCharacterException::getInstance);
  }

  private void deleteImageFromStorage(String imageUrl) {
    String objectName = extractObjectNameFromUrl(imageUrl);
    fileStorage.delete(objectName);
  }

  private String extractObjectNameFromUrl(String imageUrl) {
    return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
  }
}
