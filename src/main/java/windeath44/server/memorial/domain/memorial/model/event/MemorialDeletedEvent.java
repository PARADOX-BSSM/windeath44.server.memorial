package windeath44.server.memorial.domain.memorial.model.event;

import windeath44.server.memorial.domain.memorial.model.Memorial;

public record MemorialDeletedEvent(
        Long memorialId,
        Long characterId,
        String creatorId,
        String content
) {
    public static MemorialDeletedEvent of(Memorial memorial, String content) {
        return new MemorialDeletedEvent(
                memorial.getMemorialId(),
                memorial.getCharacterId(),
                memorial.getCreatorId(),
                content
        );
    }
}