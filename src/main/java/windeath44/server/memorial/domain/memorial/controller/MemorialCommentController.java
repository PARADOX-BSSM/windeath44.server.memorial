package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommentRequestDto;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommentUpdateRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommentCountResponse;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommentResponse;
import windeath44.server.memorial.domain.memorial.service.MemorialCommentService;
import windeath44.server.memorial.domain.memorial.service.usecase.MemorialCommentGetUseCase;
import windeath44.server.memorial.global.dto.CursorPage;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memorials/comment")
public class MemorialCommentController {
  private final MemorialCommentService memorialCommentService;
  private final MemorialCommentGetUseCase memorialCommentGetUseCase;

  @PostMapping("/{memorial-id}")
  public ResponseEntity<ResponseDto<Void>> comment(@RequestBody MemorialCommentRequestDto dto, @RequestHeader("user-id") String userId, @PathVariable("memorial-id") Long memorialId) {
    memorialCommentService.comment(dto, userId, memorialId);
    return ResponseEntity.status(201).body(HttpUtil.success("Memorial comment is successfully created."));
  }

  @GetMapping("/{memorial-id}")
  public ResponseEntity<ResponseDto<CursorPage<MemorialCommentResponse>>> getComment(@RequestHeader(value = "user-id", required = false) String userId, @PathVariable("memorial-id") Long memorialId, @RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam(value = "size", defaultValue = "10") Integer size) {
    CursorPage<MemorialCommentResponse> memorialCommentResponsesList = memorialCommentGetUseCase.getComment(userId, memorialId, cursorId, size);
    return ResponseEntity.status(200).body(HttpUtil.success("Memorial comment is successfully Found.", memorialCommentResponsesList));
  }

  @PatchMapping("/{comment-id}")
  public ResponseEntity<ResponseDto<Void>> rewrite(@PathVariable("comment-id") Long commentId, @RequestBody MemorialCommentUpdateRequestDto dto) {
    memorialCommentService.rewrite(commentId, dto);
    return ResponseEntity.status(200).body(HttpUtil.success("Memorial comment is successfully Update."));
  }

  @DeleteMapping("/{comment-id}")
  public ResponseEntity<ResponseDto<Void>> delete(@PathVariable("comment-id") Long commentId) {
    memorialCommentService.delete(commentId);
    return ResponseEntity.status(200).body(HttpUtil.success("Memorial comment is successfully delete."));
  }

  @GetMapping("/count")
  public ResponseEntity<ResponseDto<List<MemorialCommentCountResponse>>> getCommentCount(@RequestParam(value = "size", defaultValue = "10") Integer size) {
    List<MemorialCommentCountResponse> commentCounts = memorialCommentService.getCommentCountByMemorial(size);
    return ResponseEntity.status(200).body(HttpUtil.success("Memorial comment count is successfully retrieved.", commentCounts));
  }

}
