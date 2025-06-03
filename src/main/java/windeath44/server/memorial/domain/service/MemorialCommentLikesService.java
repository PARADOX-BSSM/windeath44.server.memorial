package windeath44.server.memorial.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.mapper.MemorialCommentLikesMapper;
import windeath44.server.memorial.domain.model.MemorialCommentLikesCount;
import windeath44.server.memorial.domain.model.MemorialComment;
import windeath44.server.memorial.domain.model.MemorialCommentLikes;
import windeath44.server.memorial.domain.model.MemorialCommentLikesPrimaryKey;
import windeath44.server.memorial.domain.model.vo.MemorialCommentLikesCountList;
import windeath44.server.memorial.domain.repository.MemorialCommentLikesRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemorialCommentLikesService {
  private final MemorialCommentLikesRepository memorialCommentLikesRepository;
  private final MemorialCommentService memorialCommentService;
  private final MemorialCommentLikesMapper memorialCommentLikesMapper;

  @Transactional
  public void like(Long commentId, String userId) {
    MemorialComment comment = memorialCommentService.findCommentById(commentId);
    MemorialCommentLikesPrimaryKey memorialCommentLikesPrimaryKey = comment.likesKey(userId);
    MemorialCommentLikes memorialCommentLikes = memorialCommentLikesMapper.toMemorialCommentLikes(memorialCommentLikesPrimaryKey);
    memorialCommentLikesRepository.save(memorialCommentLikes);
  }

  @Transactional
  public void unlike(Long commentId, String userId) {
    MemorialComment comment = memorialCommentService.findCommentById(commentId);
    MemorialCommentLikesPrimaryKey memorialCommentLikesPrimaryKey = comment.likesKey(userId);
    memorialCommentLikesRepository.deleteById(memorialCommentLikesPrimaryKey);
  }

  public MemorialCommentLikesCountList getLikesCountByCommentIds(List<Long> commentIds) {
    List<MemorialCommentLikesCount> memorialCommentLikesCounts = memorialCommentLikesRepository.findLikesCountGroupedByCommentIds(commentIds);
    return MemorialCommentLikesCountList.of(memorialCommentLikesCounts);
  }

}
