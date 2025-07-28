//package windeath44.server.memorial.domain.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import windeath44.server.memorial.domain.dto.ResponseDto;
//import windeath44.server.memorial.domain.dto.response.MemorialTracingResponse;
//import windeath44.server.memorial.domain.model.MemorialTracing;
//import windeath44.server.memorial.domain.service.MemorialTraceService;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/memorial-tracing/{user-id}")
//public class MemorialTracingController {
//  private final MemorialTraceService memorialTraceService;
//
//  @GetMapping
//  public ResponseEntity<ResponseDto> getMemorialTracing(@RequestHeader("user-id") String userId, @RequestParam(value = "size", defaultValue = "5") Integer size) {
//    List<MemorialTracingResponse> memorialTracingList = memorialTraceService.findRecentByUserId(userId, size);
//    ResponseDto responseDto = new ResponseDto("Memorial Tracing is Successfully Found", memorialTracingList);
//    return ResponseEntity.ok(responseDto);
//  }
//}
