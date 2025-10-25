package windeath44.server.memorial.global.dto;

public record FileUploadUrlResponse (
        String fileUrl
) {
   public static  FileUploadUrlResponse of(String url) {
       return new FileUploadUrlResponse(url);
   }
}
