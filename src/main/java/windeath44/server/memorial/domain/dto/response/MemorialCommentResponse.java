package windeath44.server.memorial.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemorialCommentResponse (
        Long commentId,
        String userId,
        String content,
        Long likes,
        Boolean isLiked,
        Long parentId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDateTime createdAt
) {
}
