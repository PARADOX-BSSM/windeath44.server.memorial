package windeath44.server.memorial.domain.memorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.memorial.model.MemorialCommentLikes;
import windeath44.server.memorial.domain.memorial.model.MemorialCommentLikesPrimaryKey;

@Repository
public interface MemorialCommentLikesRepository extends JpaRepository<MemorialCommentLikes, MemorialCommentLikesPrimaryKey> {
}
