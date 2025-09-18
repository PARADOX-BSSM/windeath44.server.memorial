package windeath44.server.memorial.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.model.MemorialCommentLikes;
import windeath44.server.memorial.domain.model.MemorialCommentLikesPrimaryKey;

import java.util.List;
import java.util.Set;

@Repository
public interface MemorialCommentLikesRepository extends JpaRepository<MemorialCommentLikes, MemorialCommentLikesPrimaryKey> {

  @Query("SELECT l.memorialCommentLikesPrimaryKey.comment.commentId FROM MemorialCommentLikes l WHERE l.memorialCommentLikesPrimaryKey.userId = :userId AND l.memorialCommentLikesPrimaryKey.comment.commentId IN :commentIds")
  Set<Long> findLikedCommentIdsByUserIdAndCommentIds(
          @Param("userId") String userId,
          @Param("commentIds") Set<Long> commentIds
  );
}
