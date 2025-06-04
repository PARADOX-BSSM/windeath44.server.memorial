package windeath44.server.memorial.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import windeath44.server.memorial.domain.dto.ResponseDto;
import windeath44.server.memorial.domain.dto.request.MemorialBowRequestDto;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialBowController {
  @PostMapping("/bow")
  public ResponseEntity<ResponseDto> bow(@RequestBody MemorialBowRequestDto memorialBowRequestDto) {

    return ResponseEntity.ok(new ResponseDto("UserId " + memorialBowRequestDto.userId() + " Successfully bowed to " + memorialBowRequestDto.memorialId(), false));
  }
}
