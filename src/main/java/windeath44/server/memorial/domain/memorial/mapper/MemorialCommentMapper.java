package windeath44.server.memorial.domain.memorial.mapper;

import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommentCountResponse;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommentResponse;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialComment;
import windeath44.server.memorial.domain.memorial.repository.projection.MemorialCommentCountProjection;

import java.util.List;
import java.util.Set;

@Component
public class MemorialCommentMapper {
  public MemorialComment toMemorialComment(Memorial memorial , String userId, String content, MemorialComment memorialComment) {
    return MemorialComment.of(memorial, userId, content, memorialComment);
  }

  public MemorialCommentResponse toMemorialCommentResponse(
          MemorialComment comment,
          Set<Long> likedCommentIds
  ) {
    boolean isLiked = likedCommentIds.contains(comment.getCommentId());

    List<MemorialCommentResponse> children = comment.getChildren().stream()
            .map(child -> toMemorialCommentResponse(child, likedCommentIds))
            .toList();

    return MemorialCommentResponse.builder()
            .commentId(comment.getCommentId())
            .userId(comment.getUserId())
            .content(comment.getContent())
            .createdAt(comment.getCreatedAt())
            .likes(comment.getLikesCount())
            .isLiked(isLiked)
            .parentId(comment.getParentCommentId())
            .children(children)
            .build();
  }

    public MemorialCommentCountResponse toMemorialCommentCountResponse(MemorialCommentCountProjection memorialCommentCountProjection) {
      MemorialCommentCountResponse memorialCommentCountResponse = MemorialCommentCountResponse.builder()
              .memorialId(memorialCommentCountProjection.getMemorialId())
              .commentCount(memorialCommentCountProjection.getCommentCount())
              .build();
      return memorialCommentCountResponse;
    }
}
