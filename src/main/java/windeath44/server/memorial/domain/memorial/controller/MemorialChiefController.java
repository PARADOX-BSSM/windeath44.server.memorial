package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.memorial.dto.ResponseDto;
import windeath44.server.memorial.domain.memorial.exception.AuthenticationFailedException;
import windeath44.server.memorial.domain.memorial.service.MemorialChiefService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memorials/chiefs")
public class MemorialChiefController {
  private final MemorialChiefService memorialChiefService;

  @GetMapping("/{memorialId}")
  public ResponseEntity<ResponseDto> getChiefs(@PathVariable Long memorialId) {
    return ResponseEntity.ok(new ResponseDto("Memorial chiefs in memorial Id: " + memorialId + " are successfully found", memorialChiefService.getChiefs(memorialId)));
  }

  @GetMapping("/update")
  public ResponseEntity<ResponseDto> update(@RequestHeader("role") String role) {
    if (!role.equals("ADMIN")) {
      throw new AuthenticationFailedException();
    }
    memorialChiefService.updateChiefs();
    return ResponseEntity.ok(new ResponseDto("Memorial chiefs are successfully updated", null));
  }
}
