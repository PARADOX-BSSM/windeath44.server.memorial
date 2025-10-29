package windeath44.server.memorial.domain.memorial.dto.request;

public record MemorialTracingUpdateDurationRequestDto(
        String memorialTracingId,
        int durationSeconds
) {
}
