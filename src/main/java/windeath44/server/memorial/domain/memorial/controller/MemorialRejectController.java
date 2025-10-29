package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialRejectRequestDto;
import windeath44.server.memorial.domain.memorial.service.MemorialRejectService;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialRejectController {
  private final MemorialRejectService memorialRejectService;

  @PatchMapping("/reject")
  public ResponseEntity<ResponseDto<Void>> reject(
          @RequestHeader("user-id") String userId,
          @RequestBody MemorialRejectRequestDto memorialRejectRequestDto
  ) {
    memorialRejectService.rejectMemorialPullRequest(userId, memorialRejectRequestDto);
    return ResponseEntity.ok(HttpUtil.success("Memorial Pull Request is Successfully Rejected"));
  }
}
