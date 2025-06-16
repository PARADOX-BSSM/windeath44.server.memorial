package windeath44.server.memorial.domain.mapper;

import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.model.MemorialTracing;
import windeath44.server.memorial.domain.model.event.MemorialTracingEvent;

@Component
public class MemorialTracingMapper {
  public MemorialTracing toMemorialTracing(MemorialTracingEvent event) {
    return MemorialTracing.of(event.memorialId(), event.userId());
  }
}
