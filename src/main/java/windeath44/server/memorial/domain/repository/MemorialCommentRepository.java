package windeath44.server.memorial.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.MemorialComment;

import java.util.List;

@Repository
public interface MemorialCommentRepository extends JpaRepository<MemorialComment, Long> {
  List<MemorialComment> memorial(Memorial memorial);

  @Query("select mc from MemorialComment mc join fetch mc.parentComment where mc.parentComment is null and mc.memorial.memorialId = :memorialId and mc.commentId <= :cursorId")
  Slice<MemorialComment> findRootComment(@Param("memorialId") Long memorialId, @Param("cursorId") Long cursorId, Pageable pageable);

  @Query("select mc from MemorialComment mc join fetch mc.parentComment where mc.parentComment.commentId in :rootIds")
  List<MemorialComment> findAllByParentCommentId(List<Long> rootIds);
}
