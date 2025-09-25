package windeath44.server.memorial.domain.memorial.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import windeath44.server.memorial.domain.memorial.model.MemorialTracing;

public interface MemorialTracingRepository extends MongoRepository<MemorialTracing, String>, MemorialTracingCustomRepository {
}

