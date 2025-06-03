package windeath44.server.memorial.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import windeath44.server.memorial.domain.dto.ResponseDto;
import windeath44.server.memorial.domain.dto.request.MemorialBowRequestDto;
import windeath44.server.memorial.domain.service.MemorialBowService;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialBowController {
  private final MemorialBowService memorialBowService;

  @PostMapping("/bow")
  public ResponseEntity<ResponseDto> bow(@RequestBody MemorialBowRequestDto memorialBowRequestDto) {
    memorialBowService.bow(memorialBowRequestDto);
    return ResponseEntity.ok(new ResponseDto("User id: " + memorialBowRequestDto.userId() + " successfully bowed to memorial id:" + memorialBowRequestDto.memorialId(), null));
  }
}
