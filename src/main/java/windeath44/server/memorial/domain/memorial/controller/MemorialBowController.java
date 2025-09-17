package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.memorial.dto.ResponseDto;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialBowRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialBowResponseDto;
import windeath44.server.memorial.domain.memorial.service.MemorialBowService;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialBowController {
  private final MemorialBowService memorialBowService;

  @PostMapping("/bow")
  public ResponseEntity<ResponseDto> bow(@RequestHeader("user-id") String userId, @RequestBody MemorialBowRequestDto memorialBowRequestDto) {
    memorialBowService.bow(userId, memorialBowRequestDto);
    return ResponseEntity.status(201).body(new ResponseDto("User id: " + userId + " successfully bowed to memorial id:" + memorialBowRequestDto.memorialId(), null));
  }

  @GetMapping("/bow/{memorialId}")
  public ResponseEntity<ResponseDto> getBowByUserId(@PathVariable Long memorialId) {
    Long bowCount = memorialBowService.bowCountByMemorialId(memorialId);
    return ResponseEntity.ok(new ResponseDto("Memorial id: " + memorialId + " bow count: " + bowCount, bowCount));
  }

  @GetMapping("/bow/{userId}/{memorialId}")
  public ResponseEntity<ResponseDto> getBowByUserIdAndMemorialId(@PathVariable String userId, @PathVariable Long memorialId) {
    MemorialBowResponseDto memorialBowResponseDto = memorialBowService.findMemorialBowByUserIdAndMemorialId(userId, memorialId);
    return ResponseEntity.ok(new ResponseDto("Memorial id: " + memorialId + " and user id: " + userId, memorialBowResponseDto));
  }
}
