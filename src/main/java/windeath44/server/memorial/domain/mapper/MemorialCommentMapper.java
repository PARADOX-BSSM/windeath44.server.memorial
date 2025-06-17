package windeath44.server.memorial.domain.mapper;

import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.dto.request.MemorialCommentRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialCommentResponse;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.MemorialComment;

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
}
