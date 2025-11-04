package windeath44.server.memorial.domain.memorial.mapper;

import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommentCountResponse;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommentResponse;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialComment;
import windeath44.server.memorial.domain.memorial.repository.projection.MemorialCommentCountProjection;
import windeath44.server.memorial.domain.memorial.repository.projection.MemorialCommentWithLikeProjection;

import java.util.List;

@Component
public class MemorialCommentMapper {
  public MemorialComment toMemorialComment(Memorial memorial , String userId, String content, MemorialComment memorialComment) {
    return MemorialComment.of(memorial, userId, content, memorialComment);
  }

  public MemorialCommentResponse toMemorialCommentResponse(MemorialCommentWithLikeProjection projection) {
    MemorialComment comment = projection.getComment();
    Boolean isLiked = projection.getIsLiked();

    List<MemorialCommentResponse> children = comment.getChildren().stream()
            .map(child -> toMemorialCommentResponseForChild(child))
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

  private MemorialCommentResponse toMemorialCommentResponseForChild(MemorialComment child) {
    return MemorialCommentResponse.builder()
            .commentId(child.getCommentId())
            .userId(child.getUserId())
            .content(child.getContent())
            .createdAt(child.getCreatedAt())
            .likes(child.getLikesCount())
            .isLiked(false)
            .parentId(child.getParentCommentId())
            .children(List.of())
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
