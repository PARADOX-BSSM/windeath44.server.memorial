package windeath44.server.memorial.domain.character.service.usecase;

import org.springframework.transaction.annotation.Transactional;
import windeath44.server.memorial.domain.character.exception.UploadFileFailException;
import windeath44.server.memorial.domain.character.service.CharacterCommandService;
import windeath44.server.memorial.global.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CharacterImageUploadUseCase {
  private final FileStorage fileStorage;
  private final CharacterCommandService characterService;

  @Transactional
  public void upload(Long characterId, MultipartFile image) {
    String imageUrl = "";
    try {
      imageUrl = fileStorage.upload(characterId.toString(), image);
    } catch(IOException e) {
      throw UploadFileFailException.getInstance();
    }
    characterService.updateImage(characterId, imageUrl);
  }
}
