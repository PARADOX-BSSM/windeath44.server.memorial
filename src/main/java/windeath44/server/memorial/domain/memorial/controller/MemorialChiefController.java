package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.exception.AuthenticationFailedException;
import windeath44.server.memorial.domain.memorial.service.MemorialChiefService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memorials/chiefs")
public class MemorialChiefController {
  private final MemorialChiefService memorialChiefService;

  @GetMapping("/{memorialId}")
  public ResponseEntity<ResponseDto<Object>> getChiefs(@PathVariable Long memorialId) {
    return ResponseEntity.ok(HttpUtil.success("Memorial chiefs in memorial Id: " + memorialId + " are successfully found", memorialChiefService.getChiefs(memorialId)));
  }

  @GetMapping("/update")
  public ResponseEntity<ResponseDto<Void>> update(@RequestHeader("role") String role) {
    if (!role.equals("ADMIN")) {
      throw new AuthenticationFailedException();
    }
    memorialChiefService.updateChiefs();
    return ResponseEntity.ok(HttpUtil.success("Memorial chiefs are successfully updated"));
  }
}
