package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialMergeableResponseDto;
import windeath44.server.memorial.domain.memorial.service.MemorialMergeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memorials")
public class MemorialMergeController {
  private final MemorialMergeService memorialMergeService;

  @PostMapping("/mergeable")
  public ResponseEntity<ResponseDto<MemorialMergeableResponseDto>> mergeable(@RequestHeader("user-id") String userId, @RequestBody MemorialMergeRequestDto dto) {
    MemorialMergeableResponseDto memorialMergeableResponseDto = memorialMergeService.validateMergeable(userId, dto);
    Boolean mergeable = memorialMergeableResponseDto.mergeable();
    if(mergeable) {
      return ResponseEntity.status(200).body(HttpUtil.success("Memorial Pull Request is mergeable.", memorialMergeableResponseDto));
    }
    return ResponseEntity.status(200).body(HttpUtil.success("Memorial Pull Request cannot be merged automatically.", memorialMergeableResponseDto));
  }

  @PatchMapping("/merge")
  public ResponseEntity<ResponseDto<Void>> merge(@RequestHeader("user-id") String userId, @RequestBody MemorialMergeRequestDto dto) {
    memorialMergeService.mergeMemorialCommit(userId, dto);
    return ResponseEntity.status(200).body(HttpUtil.success("Memorial Pull Request is successfully merged."));
  }

}
