package windeath44.server.memorial.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import windeath44.server.memorial.domain.model.MemorialChiefs;

public interface MemorialChiefsRepository extends JpaRepository<MemorialChiefs, Long> {
  void deleteAllByMemorial_MemorialId(Long memorialMemorialId);
}
