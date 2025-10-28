package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialTracingUpdateDurationRequestDto;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.dto.CursorPage;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialTracingResponse;
import windeath44.server.memorial.domain.memorial.service.MemorialTraceService;
import windeath44.server.memorial.domain.memorial.exception.InvalidCursorFormatException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/memorials/tracing")
public class MemorialTracingController {
  private final MemorialTraceService memorialTraceService;

  @GetMapping // 삭제 예쩡(deprecated)
  public ResponseEntity<ResponseDto<CursorPage<MemorialTracingResponse>>> getMemorialTracing(@RequestHeader("user-id") String userId, @RequestParam(value = "size", defaultValue = "5") int size, @RequestParam(value = "cursor", required = false) String cursor) {
    CursorPage<MemorialTracingResponse> cursorPage;

    if (cursor != null) {
      try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm");
        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Seoul"));
        Date cursorDate = dateFormat.parse(cursor);
        cursorPage = memorialTraceService.findRecentByUserIdWithCursor(userId, size, cursorDate);
      } catch (ParseException e) {
        throw new InvalidCursorFormatException();
      }
    } else {
      cursorPage = memorialTraceService.findRecentByUserId(userId, size);
    }

    return ResponseEntity.ok(HttpUtil.success("Memorial Tracing is Successfully Found", cursorPage));
  }

  @GetMapping("/recent")
  public ResponseEntity<ResponseDto<List<MemorialTracingResponse>>> getRecentMemorialTracing(
          @RequestHeader("user-id") String userId,
          @RequestParam(value = "day", defaultValue = "5") int day
  ) {
    List<MemorialTracingResponse> responses = memorialTraceService.findRecentMemorialTracingByDay(userId, day);
    return ResponseEntity.ok(HttpUtil.success("Recent Memorial Tracing is Successfully Found", responses));
  }


  @PatchMapping("/duration")
  public ResponseEntity<ResponseDto<Void>> updateDurationSeconds(
          @RequestHeader("user-id") String userId,
          @RequestBody MemorialTracingUpdateDurationRequestDto requestDto
  ) {
    memorialTraceService.updateDurationSeconds(userId, requestDto);
    return ResponseEntity.ok(HttpUtil.success("Duration updated successfully", null));
  }
}