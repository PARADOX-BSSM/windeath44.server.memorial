package windeath44.server.memorial.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.model.MemorialCommit;

import java.util.List;

@Repository
public interface MemorialCommitRepository extends JpaRepository<MemorialCommit, Long> {
  List<MemorialCommit> findMemorialCommitsByMemorial_MemorialId(Long memorialMemorialId);
}
