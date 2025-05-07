package windeath44.server.memorial.domain.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.presentation.dto.ResponseDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialResponseDto;
import windeath44.server.memorial.domain.service.MemorialGetService;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialGetController {
  private final MemorialGetService memorialGetService;

  @GetMapping("/{memorialId}")
  public ResponseEntity<ResponseDto> findByMemorialId(@PathVariable Long memorialId) {
    MemorialResponseDto memorialResponseDto = memorialGetService.findMemorialById(memorialId);
    return ResponseEntity.ok(new ResponseDto("memorialId: " + memorialId + " Successfully Found", memorialResponseDto));
  }
}
