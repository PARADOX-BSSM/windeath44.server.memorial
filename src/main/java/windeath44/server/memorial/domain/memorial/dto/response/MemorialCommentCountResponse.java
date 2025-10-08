package windeath44.server.memorial.domain.memorial.dto.response;

import lombok.Builder;

@Builder
public record MemorialCommentCountResponse(
        Long memorialId,
        Long commentCount
) {
}