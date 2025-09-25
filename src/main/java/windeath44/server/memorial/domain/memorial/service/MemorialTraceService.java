package windeath44.server.memorial.domain.memorial.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.global.dto.CursorPage;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialTracingResponse;
import windeath44.server.memorial.domain.memorial.mapper.MemorialTracingMapper;
import windeath44.server.memorial.domain.memorial.model.MemorialTracing;
import windeath44.server.memorial.domain.memorial.model.event.MemorialTracingEvent;
import windeath44.server.memorial.domain.memorial.repository.MemorialTracingRepository;
import java.util.Date;
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

  public CursorPage<MemorialTracingResponse> findRecentByUserId(String userId, int size) {
    List<MemorialTracing> memorialTracings = memorialTracingRepository.findRecentByUserId(userId, size + 1);

    boolean hasNext = memorialTracings.size() > size;
    if (hasNext) {
      memorialTracings = memorialTracings.subList(0, size);
    }

    List<MemorialTracingResponse> responses = memorialTracings.stream()
            .map(memorialTracingMapper::toMemorialTracingResponse)
            .toList();

    return new CursorPage<>(hasNext, responses);
  }

  public CursorPage<MemorialTracingResponse> findRecentByUserIdWithCursor(String userId, int size, Date cursor) {
    List<MemorialTracing> memorialTracings = memorialTracingRepository.findRecentByUserIdWithCursor(userId, cursor, size + 1);

    boolean hasNext = memorialTracings.size() > size;
    if (hasNext) {
      memorialTracings = memorialTracings.subList(0, size);
    }

    List<MemorialTracingResponse> responses = memorialTracings.stream()
            .map(memorialTracingMapper::toMemorialTracingResponse)
            .toList();

    return new CursorPage<>(hasNext, responses);
  }
}

