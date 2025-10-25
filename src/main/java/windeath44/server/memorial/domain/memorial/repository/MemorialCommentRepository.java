package windeath44.server.memorial.domain.memorial.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.memorial.model.MemorialComment;
import windeath44.server.memorial.domain.memorial.repository.projection.MemorialCommentCountProjection;
import windeath44.server.memorial.domain.memorial.repository.projection.MemorialCommentWithLikeProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemorialCommentRepository extends JpaRepository<MemorialComment, Long> {



  @Query("select mc from MemorialComment mc where mc.parentComment is null and mc.memorial.memorialId = :memorialId and mc.commentId <= :cursorId order by mc.commentId desc")
  Slice<MemorialComment> findRootCommentByCursorId(@Param("memorialId") Long memorialId, @Param("cursorId") Long cursorId, Pageable pageable);

  @Query("select mc from MemorialComment mc where mc.parentComment is null and mc.memorial.memorialId = :memorialId order by mc.commentId desc")
  Slice<MemorialComment> findRootComment(Long memorialId, Pageable pageable);

  @Query("select mc from MemorialComment mc left join fetch mc.parentComment where mc.commentId = :commentId")
  Optional<MemorialComment> findFetchById(@Param("commentId") Long commentId);

  @Query("select mc.memorial.memorialId as memorialId, count(mc) as commentCount from MemorialComment mc group by mc.memorial.memorialId order by count(mc) asc")
  List<MemorialCommentCountProjection> countCommentsByMemorial(Pageable pageable);

  @Query("select mc as comment, case when mcl.id.userId is not null then true else false end as isLiked " +
         "from MemorialComment mc " +
         "left join MemorialCommentLikes mcl on mc.commentId = mcl.id.commentId and mcl.id.userId = :userId " +
         "where mc.memorial.memorialId = :memorialId and mc.commentId <= :cursorId and mc.parentComment is null " +
         "order by mc.commentId desc")
  Slice<MemorialCommentWithLikeProjection> findRootCommentWithLikesByCursorId(@Param("memorialId") Long memorialId, @Param("userId") String userId, @Param("cursorId") Long cursorId, Pageable pageable);

  @Query("select mc as comment, case when mcl.id.userId is not null then true else false end as isLiked " +
         "from MemorialComment mc " +
         "left join MemorialCommentLikes mcl on mc.commentId = mcl.id.commentId and mcl.id.userId = :userId " +
         "where mc.memorial.memorialId = :memorialId and mc.parentComment is null " +
         "order by mc.commentId desc")
  Slice<MemorialCommentWithLikeProjection> findRootCommentWithLikes(@Param("memorialId") Long memorialId, @Param("userId") String userId, Pageable pageable);

  @Query("select mc as comment, case when mcl.id.userId is not null then true else false end as isLiked " +
         "from MemorialComment mc " +
         "left join MemorialCommentLikes mcl on mc.commentId = mcl.id.commentId and mcl.id.userId = :userId " +
         "where mc.memorial.memorialId = :memorialId and mc.parentComment is null " +
         "order by (size(mc.children) * 0.3 + mc.likesCount * 0.7) desc")
  List<MemorialCommentWithLikeProjection> findPopularRootCommentWithLikes(@Param("memorialId") Long memorialId, @Param("userId") String userId, Pageable pageable);
}
