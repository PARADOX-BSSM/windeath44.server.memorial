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
import windeath44.server.memorial.domain.memorial.service.MemorialGetServiceCacheProxy;

import java.util.List;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialGetController {
  private final MemorialGetService memorialGetService;
  private final MemorialGetServiceCacheProxy memorialGetServiceCacheProxy;

  @GetMapping("/{memorialId:\\d+}")
  public ResponseEntity<ResponseDto<MemorialResponseDto>> findByMemorialId(@PathVariable Long memorialId, @RequestHeader(value = "user-id", required = false) String userId) {
    MemorialResponseDto memorialResponseDto = memorialGetServiceCacheProxy.findMemorialById(memorialId, userId);
    return ResponseEntity.ok(HttpUtil.success("memorialId: " + memorialId + " Successfully Found", memorialResponseDto));
  }

  @GetMapping("/memorialIds")
  public ResponseEntity<ResponseDto<List<MemorialResponseDto>>> findByMemorialIds(@RequestParam List<Long> memorialIds) {
    List<MemorialResponseDto> memorialResponseDto = memorialGetService.findMemorialByIds(memorialIds);
    return ResponseEntity.ok(HttpUtil.success("memorialId: " + memorialResponseDto.getFirst().memorialId() + " Successfully Found", memorialResponseDto));
  }

  @GetMapping("")
  public ResponseEntity<ResponseDto<OffsetPage<MemorialListResponseDto>>> findAll(
          @RequestParam String orderBy,
          @RequestParam Long page
  ) {
    OffsetPage<MemorialListResponseDto> memorialListResponseDtoPage = memorialGetService.findMemorials(orderBy, page);
    return ResponseEntity.ok(HttpUtil.success("Memorials Successfully Found Order By : " + orderBy + ", Page : " + page, memorialListResponseDtoPage));
  }

  @PostMapping("/character-filtered")
  public ResponseEntity<ResponseDto<OffsetPage<MemorialListResponseDto>>> findFiltered(
          @RequestBody MemorialCharacterFilterRequestDto memorialCharacterFilterRequestDto
          ) {
    String orderBy = memorialCharacterFilterRequestDto.orderBy();
    Long page = memorialCharacterFilterRequestDto.page();
    List<Long> characters = memorialCharacterFilterRequestDto.characters();
    OffsetPage<MemorialListResponseDto> memorialListResponseDtoPage = memorialGetService.findMemorialsFiltered(orderBy, page, characters);
    return ResponseEntity.ok(HttpUtil.success("Memorials Successfully Found Order By : " + orderBy + ", Page : " + page + ", With Filter : " + characters, memorialListResponseDtoPage));
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
