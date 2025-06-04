package windeath44.server.memorial.domain.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.dto.response.MemorialCommentResponse;
import windeath44.server.memorial.domain.mapper.MemorialCommentMapper;
import windeath44.server.memorial.domain.model.MemorialComment;
import windeath44.server.memorial.domain.model.vo.MemorialCommentLikesCountList;
import windeath44.server.memorial.domain.service.MemorialCommentLikesService;
import windeath44.server.memorial.domain.service.MemorialCommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemorialCommentFacade {
  private final MemorialCommentService memorialCommentService;
  private final MemorialCommentLikesService memorialCommentLikesService;
  private final MemorialCommentMapper memorialCommentMapper;

  public List<MemorialCommentResponse> getComment(String userId) {
    List<MemorialComment> memorialCommentList = memorialCommentService.getComment();
    List<Long> memorialCommentIds = memorialCommentList.stream()
            .map(MemorialComment::getCommentId)
            .toList();

    MemorialCommentLikesCountList memorialCommentLikesCountList = memorialCommentLikesService.getLikesCountByCommentIds(memorialCommentIds, userId);

    List<MemorialCommentResponse> memorialCommentResponseList = memorialCommentList.stream()
            .map(memorialComment -> {
              Long likes = memorialCommentLikesCountList.getLike(memorialComment.getCommentId());
              Boolean isLiked = memorialCommentLikesCountList.getIsLiked(memorialComment.getCommentId());
              MemorialCommentResponse memorialCommentResponse = memorialCommentMapper.toMemorialCommentResponse(memorialComment, likes, isLiked);
              return memorialCommentResponse;
            })
            .toList();
    return memorialCommentResponseList;


  }
}
