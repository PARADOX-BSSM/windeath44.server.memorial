package windeath44.server.memorial.domain.memorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.memorial.model.MemorialBow;

import java.util.List;

@Repository
public interface MemorialBowRepository extends JpaRepository<MemorialBow, Long> {
  MemorialBow findMemorialBowByUserIdAndMemorialId(String userId, Long memorialId);
  @Query("select sum(m.bowCount) from MemorialBow m where m.memorialId = :memorialId")
  Long sumBowCount(@Param("memorialId") Long memorialId);

  @Query("select userId from MemorialBow where memorialId = :memorialId order by bowCount desc limit 3")
  List<String> top3UserIds(@Param("memorialId") Long memorialId);
}
