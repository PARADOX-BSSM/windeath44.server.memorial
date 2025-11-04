package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.mapper.MemorialCommentLikesMapper;
import windeath44.server.memorial.domain.memorial.model.MemorialComment;
import windeath44.server.memorial.domain.memorial.model.MemorialCommentLikes;
import windeath44.server.memorial.domain.memorial.model.MemorialCommentLikesPrimaryKey;
import windeath44.server.memorial.domain.memorial.repository.MemorialCommentLikesRepository;

@Service
@RequiredArgsConstructor
public class MemorialCommentLikesService {
  private final MemorialCommentLikesRepository memorialCommentLikesRepository;
  private final MemorialCommentService memorialCommentService;
  private final MemorialCommentLikesMapper memorialCommentLikesMapper;

  @Transactional
  public void like(Long commentId, String userId) {
    MemorialComment comment = memorialCommentService.findCommentById(commentId);

    MemorialCommentLikesPrimaryKey key = MemorialCommentLikesPrimaryKey.of(commentId, userId);
    if (memorialCommentLikesRepository.existsById(key)) return;

    MemorialCommentLikes memorialCommentLikes = memorialCommentLikesMapper.toMemorialCommentLikes(userId, comment);
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
}
