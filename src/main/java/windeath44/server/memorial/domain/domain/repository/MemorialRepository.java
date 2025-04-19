package windeath44.server.memorial.domain.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.domain.Memorial;

import java.util.List;

@Repository
public interface MemorialRepository extends JpaRepository<Memorial, Long> {
  @Query(value = "select * from " +
          "memorial join memorial_update_history history on memorial.memorial_id = history.memorial_id join " +
          "memorial_commit commit on history.memorial_commit_id = commit.memorial_commit_id ", nativeQuery = true)
  List<Object[]> findMemorials();


}
