package windeath44.server.memorial.global.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorage {
  String upload(String objectName, MultipartFile file) throws IOException;
  void delete(String objectName);
}
