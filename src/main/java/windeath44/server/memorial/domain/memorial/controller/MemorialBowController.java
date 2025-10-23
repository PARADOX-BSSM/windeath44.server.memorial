package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialBowRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialBowResponseDto;
import windeath44.server.memorial.domain.memorial.service.MemorialBowService;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialBowController {
  private final MemorialBowService memorialBowService;

  @PostMapping("/bow")
  public ResponseEntity<ResponseDto<Void>> bow(@RequestHeader("user-id") String userId, @RequestBody MemorialBowRequestDto memorialBowRequestDto) {
    memorialBowService.bow(userId, memorialBowRequestDto);
    return ResponseEntity.status(201).body(HttpUtil.success("User id: " + userId + " successfully bowed to memorial id:" + memorialBowRequestDto.memorialId()));
  }

  @GetMapping("/bow/{memorialId}")
  public ResponseEntity<ResponseDto<Long>> getBowByUserId(@PathVariable Long memorialId) {
    Long bowCount = memorialBowService.bowCountByMemorialId(memorialId);
    return ResponseEntity.ok(HttpUtil.success("Memorial id: " + memorialId + " bow count: " + bowCount, bowCount));
  }

  @GetMapping("/bow/{userId]}/{memorialId}")
  public ResponseEntity<ResponseDto<MemorialBowResponseDto>> getBowByUserIdAndMemorialId(@PathVariable String userId, @PathVariable Long memorialId) {
    MemorialBowResponseDto memorialBowResponseDto = memorialBowService.findMemorialBowByUserIdAndMemorialId(userId, memorialId);
    return ResponseEntity.ok(HttpUtil.success("Memorial id: " + memorialId + " and user id: " + userId, memorialBowResponseDto));
  }
}
