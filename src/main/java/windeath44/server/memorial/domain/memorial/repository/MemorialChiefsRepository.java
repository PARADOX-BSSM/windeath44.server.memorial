package windeath44.server.memorial.domain.memorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import windeath44.server.memorial.domain.memorial.model.MemorialChiefs;

import java.util.List;

public interface MemorialChiefsRepository extends JpaRepository<MemorialChiefs, Long> {

  @Query("delete from MemorialChiefs m where m.memorial.memorialId = :memorialId")
  @Modifying
  void deleteAllByMemorial_MemorialId(Long memorialId);

  List<MemorialChiefs> findByUserId(String userId);
}
