package windeath44.server.memorial.domain.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import windeath44.server.memorial.domain.presentation.dto.ResponseDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialRejectRequestDto;
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
