package windeath44.server.memorial.domain.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import windeath44.server.memorial.domain.model.MemorialTracing;

public interface MemorialTracingRepository extends MongoRepository<MemorialTracing, String> {
}
