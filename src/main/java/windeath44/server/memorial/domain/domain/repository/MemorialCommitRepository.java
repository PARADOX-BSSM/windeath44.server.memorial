package windeath44.server.memorial.domain.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.domain.Memorial;
import windeath44.server.memorial.domain.domain.MemorialCommit;
import windeath44.server.memorial.domain.domain.MemorialCommitState;

@Repository
public interface MemorialCommitRepository extends JpaRepository<MemorialCommit, Long> {
  MemorialCommit findMemorialCommitByMemorialAndState(Memorial memorial, MemorialCommitState state);
}
