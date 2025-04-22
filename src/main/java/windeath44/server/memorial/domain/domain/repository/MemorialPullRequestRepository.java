package windeath44.server.memorial.domain.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.domain.Memorial;
import windeath44.server.memorial.domain.domain.MemorialCommit;
import windeath44.server.memorial.domain.domain.MemorialCommitState;
import windeath44.server.memorial.domain.domain.MemorialPullRequest;

@Repository
public interface MemorialPullRequestRepository extends JpaRepository<MemorialPullRequest, Long> {
  MemorialPullRequest findByMemorialCommit(MemorialCommit commit);

  MemorialPullRequest findMemorialPullRequestByMemorialAndState(Memorial memorial, MemorialCommitState memorialCommitState);
}
