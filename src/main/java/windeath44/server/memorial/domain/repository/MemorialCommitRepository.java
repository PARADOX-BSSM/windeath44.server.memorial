package windeath44.server.memorial.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.model.MemorialCommit;

@Repository
public interface MemorialCommitRepository extends JpaRepository<MemorialCommit, Long> {
}
