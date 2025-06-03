package windeath44.server.memorial.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.model.MemorialComment;

@Repository
public interface MemorialCommentRepository extends JpaRepository<MemorialComment, Long> {
}
