package windeath44.server.memorial.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.model.MemorialBow;

@Repository
public interface MemorialBowRepository extends JpaRepository<MemorialBow, Long> {
  MemorialBow findMemorialBowByUserId(String userId);
}
