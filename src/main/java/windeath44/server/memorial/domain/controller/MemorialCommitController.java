package windeath44.server.memorial.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.dto.ResponseDto;
import windeath44.server.memorial.domain.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.service.MemorialCommitService;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialCommitController {
  private final MemorialCommitService memorialCommitService;

  @PostMapping("/commit")
  public ResponseEntity<ResponseDto> commit(@RequestBody MemorialCommitRequestDto dto) {
    memorialCommitService.createMemorialCommit(dto);
    return ResponseEntity.status(201).body(new ResponseDto("Memorial Commit is successfully created.", null));
  }
}