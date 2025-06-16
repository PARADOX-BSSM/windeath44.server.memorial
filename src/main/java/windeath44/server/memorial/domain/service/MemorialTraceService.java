package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.mapper.MemorialTracingMapper;
import windeath44.server.memorial.domain.model.MemorialTracing;
import windeath44.server.memorial.domain.model.event.MemorialTracingEvent;
import windeath44.server.memorial.domain.repository.MemorialTracingRepository;

@Service
@RequiredArgsConstructor
public class MemorialTraceService {
  private final MemorialTracingMapper memorialTracingMapper;
  private final MemorialTracingRepository memorialTracingRepository;

  @Async
  @EventListener
  public void memorialTracing(MemorialTracingEvent event) {
    MemorialTracing memorialTracing = memorialTracingMapper.toMemorialTracing(event);
    memorialTracingRepository.save(memorialTracing);
  }

}
