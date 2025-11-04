package windeath44.server.memorial.global.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import windeath44.server.memorial.global.dto.FileUploadUrlResponse;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileUploader {
    private final FileStorage fileStorage;


    @Transactional
    public FileUploadUrlResponse upload(String objectName, MultipartFile file) {
        String fileUrl = "";
        try {
            fileUrl = fileStorage.upload(objectName, file);
            return FileUploadUrlResponse.of(fileUrl);
        } catch(IOException e) {
            throw UploadFileFailException.getInstance();
        }
    }
}
