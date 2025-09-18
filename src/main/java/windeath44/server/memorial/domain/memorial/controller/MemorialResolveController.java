package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialResolveRequestDto;
import windeath44.server.memorial.domain.memorial.service.MemorialResolveService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memorials")
public class MemorialResolveController {
  private final MemorialResolveService memorialResolveService;

  @PatchMapping("/resolve")
  public ResponseEntity<ResponseDto<Void>> resolve(
          @RequestHeader("user-id") String userId,
          @RequestBody MemorialResolveRequestDto memorialResolveRequestDto
          ) {
    memorialResolveService.resolve(userId, memorialResolveRequestDto);
    return ResponseEntity.ok(HttpUtil.success("Memorial Pull Request is successfully resolved and merged"));
  }
}
