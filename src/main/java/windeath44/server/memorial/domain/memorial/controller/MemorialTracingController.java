package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.dto.CursorPage;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialTracingResponse;
import windeath44.server.memorial.domain.memorial.service.MemorialTraceService;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/memorial-tracing")
public class MemorialTracingController {
  private final MemorialTraceService memorialTraceService;

  @GetMapping
  public ResponseEntity<ResponseDto<CursorPage<MemorialTracingResponse>>> getMemorialTracing(@RequestHeader("user-id") String userId, @RequestParam(value = "size", defaultValue = "5") int size, @RequestParam(value = "cursor", required = false) Long cursor) {
    CursorPage<MemorialTracingResponse> cursorPage;

    if (cursor != null) {
      Date cursorDate = new Date(cursor);
      cursorPage = memorialTraceService.findRecentByUserIdWithCursor(userId, size, cursorDate);
    } else {
      cursorPage = memorialTraceService.findRecentByUserId(userId, size);
    }

    return ResponseEntity.ok(HttpUtil.success("Memorial Tracing is Successfully Found", cursorPage));
  }
}

