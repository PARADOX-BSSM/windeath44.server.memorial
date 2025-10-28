package windeath44.server.memorial.domain.memorial.mapper;

import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialTracingResponse;
import windeath44.server.memorial.domain.memorial.model.MemorialTracing;
import windeath44.server.memorial.domain.memorial.model.event.MemorialTracingEvent;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
public class MemorialTracingMapper {
  public MemorialTracing toMemorialTracing(MemorialTracingEvent event) {
    return MemorialTracing.of(event.memorialId(), event.userId());
  }

  public MemorialTracingResponse toMemorialTracingResponse(MemorialTracing memorialTracing) {
    Long memorialId = memorialTracing.getMemorialId();
    Date viewedAt = memorialTracing.getViewed();
    int durationSeconds = memorialTracing.getDurationSeconds();

    return MemorialTracingResponse.builder()
            .memorialId(memorialId)
            .viewedAt(viewedAt)
            .durationSeconds(durationSeconds)
            .build();
  }

  public List<MemorialTracingResponse> toMemorialTracingResponse(List<MemorialTracing> memorialTracings) {
    return memorialTracings.stream()
            .map(this::toMemorialTracingResponse)
            .toList();
  }
}