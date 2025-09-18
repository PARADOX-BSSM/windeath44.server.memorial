package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.mapper.MemorialCommentLikesMapper;
import windeath44.server.memorial.domain.memorial.model.MemorialComment;
import windeath44.server.memorial.domain.memorial.model.MemorialCommentLikes;
import windeath44.server.memorial.domain.memorial.model.MemorialCommentLikesPrimaryKey;
import windeath44.server.memorial.domain.memorial.repository.MemorialCommentLikesRepository;

import java.util.Collections;
import java.util.Set;

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
    if (memorialCommentLikesRepository.existsById(memorialCommentLikesPrimaryKey)) return;

    MemorialCommentLikes memorialCommentLikes = memorialCommentLikesMapper.toMemorialCommentLikes(memorialCommentLikesPrimaryKey);
    memorialCommentLikesRepository.save(memorialCommentLikes);
    comment.upLikes();
  }

  @Transactional
  public void unlike(Long commentId, String userId) {
    MemorialComment comment = memorialCommentService.findCommentById(commentId);

    MemorialCommentLikesPrimaryKey memorialCommentLikesPrimaryKey = comment.likesKey(userId);
    if (!memorialCommentLikesRepository.existsById(memorialCommentLikesPrimaryKey)) return;

    memorialCommentLikesRepository.deleteById(memorialCommentLikesPrimaryKey);
    comment.downLikes();
  }

  public Set<Long> getLikedCommentIds(String userId, Set<Long> commentIds) {
    if (commentIds == null || commentIds.isEmpty()) {
      return Collections.emptySet();
    }
    return memorialCommentLikesRepository.findLikedCommentIdsByUserIdAndCommentIds(userId, commentIds);
  }
}
