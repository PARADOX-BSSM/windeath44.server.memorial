package windeath44.server.memorial.domain.model.vo;

import lombok.AllArgsConstructor;
import windeath44.server.memorial.domain.model.MemorialCommentLikesCount;

import java.util.List;

@AllArgsConstructor
public class MemorialCommentLikesCountList {
  private List<MemorialCommentLikesCount> memorialCommentLikesCountList;

  public static MemorialCommentLikesCountList of(List<MemorialCommentLikesCount> memorialCommentLikesCountList) {
    return new MemorialCommentLikesCountList(memorialCommentLikesCountList);
  }

  public Long get(Long commentId) {
    for(MemorialCommentLikesCount memorialCommentLikesCount : memorialCommentLikesCountList) {
      if (memorialCommentLikesCount.commentId() == commentId) return memorialCommentLikesCount.likesCount();
    }
    return 0L;
  }

}
