package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.memorial.dto.response.TodayMemorialResponse;
import windeath44.server.memorial.global.dto.OffsetPage;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCharacterFilterRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialResponseDto;
import windeath44.server.memorial.domain.memorial.service.MemorialGetService;

import java.util.List;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialGetController {
  private final MemorialGetService memorialGetService;

  @GetMapping("/{memorialId}")
  public ResponseEntity<ResponseDto<MemorialResponseDto>> findByMemorialId(@PathVariable Long memorialId, @RequestHeader(value = "user-id", required = false) String userId) {
    MemorialResponseDto memorialResponseDto = memorialGetService.findMemorialById(memorialId, userId);
    return ResponseEntity.ok(HttpUtil.success("memorialId: " + memorialId + " Successfully Found", memorialResponseDto));
  }

  @GetMapping("")
  public ResponseEntity<ResponseDto<OffsetPage<MemorialListResponseDto>>> findAll(
          @RequestParam String orderBy,
          @RequestParam Long page
  ) {
    OffsetPage<MemorialListResponseDto> memorialListResponseDtoList = memorialGetService.findMemorials(orderBy, page);
    return ResponseEntity.ok(HttpUtil.success("Memorials Successfully Found Order By : " + orderBy + ", Page : " + page, memorialListResponseDtoList));
  }

  @PostMapping("/character-filtered")
  public ResponseEntity<ResponseDto<OffsetPage<MemorialListResponseDto>>> findFiltered(
          @RequestBody MemorialCharacterFilterRequestDto memorialCharacterFilterRequestDto
          ) {
    String orderBy = memorialCharacterFilterRequestDto.orderBy();
    Long page = memorialCharacterFilterRequestDto.page();
    List<Long> characters = memorialCharacterFilterRequestDto.characters();
    OffsetPage<MemorialListResponseDto> memorialListResponseDtoList = memorialGetService.findMemorialsFiltered(orderBy, page, characters);
    return ResponseEntity.ok(HttpUtil.success("Memorials Successfully Found Order By : " + orderBy + ", Page : " + page + ", With Filter : " + characters, memorialListResponseDtoList));
  }

  @GetMapping("/today-best")
  public ResponseEntity<ResponseDto<TodayMemorialResponse>> getTodayBest() {
    TodayMemorialResponse todayMemorialResponse = memorialGetService.getTodayMemorial();
    if (todayMemorialResponse.memorialId() == null) {
        return ResponseEntity.status(404).body(HttpUtil.success("memorial not found", null));
    }
    ResponseDto<TodayMemorialResponse> responseDto = HttpUtil.success("find memorial id by comments count", todayMemorialResponse);
    return ResponseEntity.ok(responseDto);
  }
}
