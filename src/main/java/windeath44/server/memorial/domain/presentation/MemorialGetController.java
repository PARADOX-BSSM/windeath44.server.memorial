package windeath44.server.memorial.domain.presentation;

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

  @GetMapping("/")
  public ResponseEntity<ResponseDto> commit(@RequestBody MemorialResponseDto dto) {
    return null;
  }
}
