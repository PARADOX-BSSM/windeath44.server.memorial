package windeath44.server.memorial.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.model.MemorialCommentLikesCount;
import windeath44.server.memorial.domain.model.MemorialCommentLikes;
import windeath44.server.memorial.domain.model.MemorialCommentLikesPrimaryKey;

import java.util.List;

@Repository
public interface MemorialCommentLikesRepository extends JpaRepository<MemorialCommentLikes, MemorialCommentLikesPrimaryKey> {

  @Query("SELECT new windeath44.server.memorial.domain.model.MemorialCommentLikesCount(mcl.memorialCommentLikesPrimaryKey.comment.commentId, COUNT(mcl)) " +
          "FROM MemorialCommentLikes mcl " +
          "WHERE mcl.memorialCommentLikesPrimaryKey.comment.commentId IN :commentIds " +
          "GROUP BY mcl.memorialCommentLikesPrimaryKey.comment.commentId")
  List<MemorialCommentLikesCount> findLikesCountGroupedByCommentIds(List<Long> commentIds);
}
