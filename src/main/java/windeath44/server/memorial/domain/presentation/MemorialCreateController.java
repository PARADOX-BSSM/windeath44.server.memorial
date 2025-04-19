package windeath44.server.memorial.domain.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.presentation.dto.global.ResponseDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialUpdateHistoryRequestDto;
import windeath44.server.memorial.domain.service.MemorialCommitPostService;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialCreateController {
  private final MemorialCommitPostService memorialCommitPostService;

  @PostMapping("/commit")
  public ResponseEntity<ResponseDto> commit(@RequestBody MemorialCommitRequestDto dto) {
    memorialCommitPostService.createMemorialCommit(dto);
    return ResponseEntity.ok(new ResponseDto(201, "Memorial Commit is successfully created.", null));
  }

  @PatchMapping("/merge")
  public ResponseEntity<ResponseDto> merge(@RequestBody MemorialUpdateHistoryRequestDto dto) {
    memorialCommitPostService.mergeMemorialCommit(dto);
    return ResponseEntity.ok(new ResponseDto(201, "Memorial Commit is successfully merged.", null));
  }
}