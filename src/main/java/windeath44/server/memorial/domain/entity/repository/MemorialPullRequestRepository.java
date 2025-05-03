package windeath44.server.memorial.domain.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.entity.Memorial;
import windeath44.server.memorial.domain.entity.MemorialCommit;
import windeath44.server.memorial.domain.entity.MemorialPullRequestState;
import windeath44.server.memorial.domain.entity.MemorialPullRequest;

@Repository
public interface MemorialPullRequestRepository extends JpaRepository<MemorialPullRequest, Long> {
  MemorialPullRequest findByMemorialCommit(MemorialCommit commit);

  MemorialPullRequest findMemorialPullRequestByMemorialAndState(Memorial memorial, MemorialPullRequestState memorialPullRequestState);
}
