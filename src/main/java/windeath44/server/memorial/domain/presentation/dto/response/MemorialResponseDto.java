package windeath44.server.memorial.domain.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record MemorialResponseDto(
        Long memorial_id,
        Long character_id,
        List<String> chiefs,
        Long bow_count,
        Long memorial_commit_id,
        String content,
        Long likes,
        LocalDateTime updated_at
) {
}
