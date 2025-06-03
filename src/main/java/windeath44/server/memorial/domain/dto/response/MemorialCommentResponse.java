package windeath44.server.memorial.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemorialCommentResponse (
        Long commentId,
        Long memorialId,
        String userId,
        String content,
        Long parentId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDateTime createdAt
) {
}
