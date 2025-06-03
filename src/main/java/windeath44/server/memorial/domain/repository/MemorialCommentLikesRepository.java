package windeath44.server.memorial.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.model.MemorialCommentLikes;
import windeath44.server.memorial.domain.model.MemorialCommentLikesPrimaryKey;

@Repository
public interface MemorialCommentLikesRepository extends JpaRepository<MemorialCommentLikes, MemorialCommentLikesPrimaryKey> {

}
