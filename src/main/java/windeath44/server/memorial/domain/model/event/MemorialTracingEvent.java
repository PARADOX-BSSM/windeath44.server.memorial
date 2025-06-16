package windeath44.server.memorial.domain.model.event;


public record MemorialTracingEvent (
        Long memorialId,
        String userId
) {
}

