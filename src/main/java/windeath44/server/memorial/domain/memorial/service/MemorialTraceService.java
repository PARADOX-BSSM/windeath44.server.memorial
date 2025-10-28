package windeath44.server.memorial.domain.memorial.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialTracingUpdateDurationRequestDto;
import windeath44.server.memorial.domain.memorial.exception.MemorialTracingNotFoundException;
import windeath44.server.memorial.domain.memorial.exception.UnauthorizedMemorialTracingAccessException;
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
    if (hasNext) memorialTracings = memorialTracings.subList(0, size);


    List<MemorialTracingResponse> responses = memorialTracingMapper.toMemorialTracingResponse(memorialTracings);

    return new CursorPage<>(hasNext, responses);
  }

  public CursorPage<MemorialTracingResponse> findRecentByUserIdWithCursor(String userId, int size, Date cursor) {
    List<MemorialTracing> memorialTracings = memorialTracingRepository.findRecentByUserIdWithCursor(userId, cursor, size + 1);

    boolean hasNext = memorialTracings.size() > size;
    if (hasNext) {
      memorialTracings = memorialTracings.subList(0, size);
    }

    List<MemorialTracingResponse> responses = memorialTracingMapper.toMemorialTracingResponse(memorialTracings);

    return new CursorPage<>(hasNext, responses);
  }


  public List<MemorialTracingResponse> findRecentMemorialTracingByDay(String userId, int day) {
    List<MemorialTracing> memorialTracings = memorialTracingRepository.findRecentByUserIdWithinDays(userId, day);

    return memorialTracingMapper.toMemorialTracingResponse(memorialTracings);
  }


  public void updateDurationSeconds(String userId, MemorialTracingUpdateDurationRequestDto requestDto) {
    String memorialTracingId = requestDto.memorialTracingId();
    int durationSeconds = requestDto.durationSeconds();

    validateEqaulsUser(userId, memorialTracingId);

    memorialTracingRepository.updateDurationSeconds(memorialTracingId, durationSeconds);
  }

  private void validateEqaulsUser(String userId, String memorialTracingId) {
    MemorialTracing memorialTracing = memorialTracingRepository.findById(memorialTracingId)
            .orElseThrow(MemorialTracingNotFoundException::new);

    if (!memorialTracing.checkOwner(userId)) {
      throw new UnauthorizedMemorialTracingAccessException();
    }
  }
}