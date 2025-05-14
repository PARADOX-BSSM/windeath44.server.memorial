package windeath44.server.memorial.domain.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.presentation.dto.ResponseDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialResponseDto;
import windeath44.server.memorial.domain.service.MemorialGetService;

import java.util.List;

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

  @GetMapping("")
  public ResponseEntity<ResponseDto> findAll(
          @RequestParam String orderBy,
          @RequestParam Long page
  ) {
    List<MemorialListResponseDto> memorialListResponseDtoList = memorialGetService.findMemorials(orderBy, page);
    return ResponseEntity.ok(new ResponseDto("Memorials Successfully Found Order By : " + orderBy + ", Page : " + page, memorialListResponseDtoList));
  }
}
