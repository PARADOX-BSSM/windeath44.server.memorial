package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.memorial.dto.ResponseDto;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.memorial.service.MemorialCommitService;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialCommitController {
  private final MemorialCommitService memorialCommitService;

  @PostMapping("/commit")
  public ResponseEntity<ResponseDto> commit(@RequestHeader("user-id") String userId, @RequestBody MemorialCommitRequestDto dto) {
    memorialCommitService.createMemorialCommit(userId, dto);
    return ResponseEntity.status(201).body(new ResponseDto("Memorial Commit is successfully created.", null));
  }
}