package windeath44.server.memorial.domain.mapper;

import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.dto.request.MemorialCommentRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialCommentResponse;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.MemorialComment;

@Component
public class MemorialCommentMapper {
  public MemorialComment toMemorialComment(Memorial memorial , String userId, String content, Long parentCommentId) {
    return MemorialComment.of(memorial, userId, content, parentCommentId);
  }

  public MemorialCommentResponse toMemorialCommentResponse(MemorialComment memorialComment, Long likes, Boolean isLiked) {
    return MemorialCommentResponse.builder()
            .commentId(memorialComment.getCommentId())
            .userId(memorialComment.getUserId())
            .content(memorialComment.getContent())
            .createdAt(memorialComment.getCreatedAt())
            .likes(likes)
            .isLiked(isLiked)
            .memorialId(memorialComment.getMemorialId())
            .parentId(memorialComment.getParentId())
            .build();
  }
}
