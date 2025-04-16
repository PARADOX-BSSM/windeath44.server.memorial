package windeath44.server.memorial.domain.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.domain.MemorialUpdateHistory;

@Repository
public interface MemorialUpdateHistoryRepository extends JpaRepository<MemorialUpdateHistory, Long> {
}
