package windeath44.server.memorial.domain.character.service.usecase;

import org.springframework.transaction.annotation.Transactional;
import windeath44.server.memorial.global.dto.FileUploadUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import windeath44.server.memorial.global.storage.FileUploader;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CharacterImageUploadUseCase {
  private final FileUploader fileUploader;

  @Transactional
  public FileUploadUrlResponse upload(String userId, MultipartFile image) {
    String uuid = UUID.randomUUID().toString();
    String characterObjectName = "characters/" + userId + "/" + uuid;
    FileUploadUrlResponse fileUploadUrlResponse = fileUploader.upload(characterObjectName, image);
    return fileUploadUrlResponse;
  }
}
