package windeath44.server.memorial.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.dto.ResponseDto;
import windeath44.server.memorial.domain.dto.request.MemorialRejectRequestDto;
import windeath44.server.memorial.domain.service.MemorialRejectService;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialRejectController {
  private final MemorialRejectService memorialRejectService;

  @PatchMapping("/reject")
  public ResponseEntity<ResponseDto> reject(@RequestBody MemorialRejectRequestDto memorialRejectRequestDto) {
    memorialRejectService.rejectMemorialPullRequest(memorialRejectRequestDto);
    return ResponseEntity.ok(new ResponseDto(
            "Memorial Pull Request is Successfully Rejected",
            null
    ));
  }
}
