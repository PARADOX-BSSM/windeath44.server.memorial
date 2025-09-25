package windeath44.server.memorial.domain.memorial.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialTracingResponse;
import windeath44.server.memorial.domain.memorial.model.MemorialTracing;

import java.util.List;

public interface MemorialTracingRepository extends MongoRepository<MemorialTracing, String> {
  @Query(value = "{ 'userId': ?0 }", sort = "{ 'createdAt': -1 }")
  List<MemorialTracing> findRecentByUserId(String userId, Pageable pageable);
}
