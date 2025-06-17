package windeath44.server.memorial.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.dto.ResponseDto;
import windeath44.server.memorial.domain.dto.request.MemorialCharacterFilterRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.dto.response.MemorialResponseDto;
import windeath44.server.memorial.domain.service.MemorialGetService;

import java.util.List;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialGetController {
  private final MemorialGetService memorialGetService;

  @GetMapping("/{memorialId}")
  public ResponseEntity<ResponseDto> findByMemorialId(@PathVariable Long memorialId, @RequestHeader(value = "user-id", required = false) String userId) {
    MemorialResponseDto memorialResponseDto = memorialGetService.findMemorialById(memorialId, userId);
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

  @PostMapping("/character-filtered")
  public ResponseEntity<ResponseDto> findFiltered(
          @RequestBody MemorialCharacterFilterRequestDto memorialCharacterFilterRequestDto
          ) {
    String orderBy = memorialCharacterFilterRequestDto.orderBy();
    Long page = memorialCharacterFilterRequestDto.page();
    List<Long> characters = memorialCharacterFilterRequestDto.characters();
    List<MemorialListResponseDto> memorialListResponseDtoList = memorialGetService.findMemorialsFiltered(orderBy, page, characters);
    return ResponseEntity.ok(new ResponseDto("Memorials Successfully Found Order By : " + orderBy + ", Page : " + page + ", With Filter : " + characters, memorialListResponseDtoList));
  }
}
