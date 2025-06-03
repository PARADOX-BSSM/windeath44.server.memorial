package windeath44.server.memorial.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.dto.ResponseDto;
import windeath44.server.memorial.domain.dto.request.MemorialCommentRequestDto;
import windeath44.server.memorial.domain.dto.request.MemorialCommentUpdateRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialCommentResponse;
import windeath44.server.memorial.domain.service.MemorialCommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memorials/comment")
public class MemorialCommentController {
  private final MemorialCommentService memorialCommentService;

  @PostMapping
  public ResponseEntity<ResponseDto> comment(@@RequestBody final MemorialCommentRequestDto dto, @RequestHeader("user-id") final String userId) {
    memorialCommentService.comment(dto, userId);
    return ResponseEntity.status(201).body(new ResponseDto("Memorial comment is successfully created.", null));
  }

  @GetMapping
  public ResponseEntity<ResponseDto> getComment() {
    List<MemorialCommentResponse> memorialCommentResponsesList = memorialCommentService.getComment();
    return ResponseEntity.status(200).body(new ResponseDto("Memorial comment is successfully Found.", memorialCommentResponsesList));
  }

  @PatchMapping("/{comment-id}")
  public ResponseEntity<ResponseDto> rewrite(@PathVariable("comment-id") Long commentId, @RequestBody MemorialCommentUpdateRequestDto dto) {
    memorialCommentService.rewrite(commentId, dto);
    return ResponseEntity.status(200).body(new ResponseDto("Memorial comment is successfully Update.", null));
  }

  @DeleteMapping("/comment-id")
  public ResponseEntity<ResponseDto> delete(@PathVariable("comment-id") Long commentId) {
    memorialCommentService.delete(commentId);
    return ResponseEntity.status(200).body(new ResponseDto("Memorial comment is successfully delete.", null));
  }
}
