package windeath44.server.memorial.domain.memorial.model.event;


public record MemorialTracingEvent (
        Long memorialId,
        String userId
) {
}

