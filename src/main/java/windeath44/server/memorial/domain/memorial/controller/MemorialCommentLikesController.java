package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.service.MemorialCommentLikesService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memorials/comment/likes/{comment-id}")
public class MemorialCommentLikesController {
  private final MemorialCommentLikesService memorialCommentLikesService;

  @PostMapping
  public ResponseEntity<ResponseDto<Void>> like(@PathVariable("comment-id") Long commentId, @RequestHeader("user-id") String userId) {
    memorialCommentLikesService.like(commentId, userId);
    return ResponseEntity.status(201).body(HttpUtil.success("Memorial comment is successfully liked."));
  }

  @DeleteMapping
  public ResponseEntity<ResponseDto<Void>> unlike(@PathVariable("comment-id") Long commentId, @RequestHeader("user-id") String userId) {
    memorialCommentLikesService.unlike(commentId, userId);
    return ResponseEntity.status(200).body(HttpUtil.success("Memorial comment is successfully unliked."));
  }
}
