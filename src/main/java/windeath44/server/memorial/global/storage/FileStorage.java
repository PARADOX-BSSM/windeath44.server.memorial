package windeath44.server.memorial.global.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorage {
  String upload(String characterId, MultipartFile file) throws IOException;

}
