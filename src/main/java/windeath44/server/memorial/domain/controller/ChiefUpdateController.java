package windeath44.server.memorial.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import windeath44.server.memorial.domain.dto.ResponseDto;
import windeath44.server.memorial.domain.exception.AuthenticationFailedException;
import windeath44.server.memorial.domain.service.ChiefUpdateService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/memorials/chiefs")
public class ChiefUpdateController {
  private final ChiefUpdateService chiefUpdateService;

  @GetMapping("")
  public ResponseEntity<ResponseDto> update(@RequestHeader("role") String role) {
    if (!role.equals("ADMIN")) {
      throw new AuthenticationFailedException();
    }
    chiefUpdateService.updateChiefs();
    return ResponseEntity.ok(new ResponseDto("Memorial chiefs are successfully updated", null));
  }

}
