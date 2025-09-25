package windeath44.server.memorial.domain.memorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialCommit;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequestState;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;

import java.util.List;

@Repository
public interface MemorialPullRequestRepository extends JpaRepository<MemorialPullRequest, Long> {
  MemorialPullRequest findByMemorialCommit(MemorialCommit commit);

  MemorialPullRequest findMemorialPullRequestByMemorialAndState(Memorial memorial, MemorialPullRequestState memorialPullRequestState);

  List<MemorialPullRequest> findMemorialPullRequestsByMemorial_MemorialId(Long memorialId);
}
