package windeath44.server.memorial.domain.memorial.repository;

import windeath44.server.memorial.domain.memorial.model.MemorialTracing;

import java.util.Date;
import java.util.List;

public interface MemorialTracingCustomRepository {
  List<MemorialTracing> findRecentByUserId(String userId, int size);
  List<MemorialTracing> findRecentByUserIdWithCursor(String userId, Date cursor, int size);

  List<MemorialTracing> findRecentByUserIdWithinDays(String userId, int days);

  void updateDurationSeconds(String memorialTracingId, int durationSeconds);
}