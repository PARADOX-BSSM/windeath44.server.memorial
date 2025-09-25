package windeath44.server.memorial.domain.memorial.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.memorial.model.MemorialComment;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemorialCommentRepository extends JpaRepository<MemorialComment, Long> {



  @Query("select mc from MemorialComment mc join fetch mc.parentComment where mc.parentComment is null and mc.memorial.memorialId = :memorialId and mc.commentId <= :cursorId order by mc.commentId desc")
  Slice<MemorialComment> findRootCommentByCursorId(@Param("memorialId") Long memorialId, @Param("cursorId") Long cursorId, Pageable pageable);

  @Query("select mc from MemorialComment mc where mc.parentComment is null and mc.memorial.memorialId = :memorialId order by mc.commentId desc")
  Slice<MemorialComment> findRootComment(Long memorialId, Pageable pageable);

  @Query("select mc from MemorialComment mc join fetch mc.parentComment where mc.parentComment.commentId in :rootIds")
  List<MemorialComment> findAllByParentCommentId(List<Long> rootIds);

  @Query("select mc from MemorialComment mc join fetch mc.parentComment where mc.commentId = :commentId")
  Optional<MemorialComment> findFetchById(@Param("commentId") Long commentId);
}
