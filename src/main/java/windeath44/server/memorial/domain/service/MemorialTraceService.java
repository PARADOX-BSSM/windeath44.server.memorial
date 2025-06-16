package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.dto.response.MemorialTracingResponse;
import windeath44.server.memorial.domain.mapper.MemorialTracingMapper;
import windeath44.server.memorial.domain.model.MemorialTracing;
import windeath44.server.memorial.domain.model.event.MemorialTracingEvent;
import windeath44.server.memorial.domain.repository.MemorialTracingRepository;

import java.util.List;

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


  public List<MemorialTracingResponse> findRecentByUserId(String userId, Integer size) {
    Pageable pageable = PageRequest.of(0, size);
    List<MemorialTracingResponse> memorialTracingResponseList = memorialTracingRepository.findRecentByUserId(userId, pageable)
            .stream()
            .map(memorialTracingMapper::toMemorialTracingResponse)
            .toList();
    return memorialTracingResponseList;
  }

}
