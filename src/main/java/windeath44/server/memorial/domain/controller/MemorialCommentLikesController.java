package windeath44.server.memorial.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.dto.ResponseDto;
import windeath44.server.memorial.domain.service.MemorialCommentLikesService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memorials/comment/likes/{comment-id}")
public class MemorialCommentLikesController {
  private final MemorialCommentLikesService memorialCommentLikesService;

  @PostMapping
  public ResponseEntity<ResponseDto> like(@PathVariable("comment-id") Long commentId, @RequestHeader("user-id") String userId) {
    userId = userId.substring(2, userId.length() - 2);
    memorialCommentLikesService.like(commentId, userId);
    return ResponseEntity.status(201).body(new ResponseDto("Memorial comment is successfully liked.", null));
  }

  @DeleteMapping
  public ResponseEntity<ResponseDto> unlike(@PathVariable("comment-id") Long commentId, @RequestHeader("user-id") String userId) {
    userId = userId.substring(2, userId.length() - 2);
    memorialCommentLikesService.unlike(commentId, userId);
    return ResponseEntity.status(200).body(new ResponseDto("Memorial comment is successfully unliked.", null));
  }
}
