package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.memorial.service.MemorialCommitService;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialCommitController {
  private final MemorialCommitService memorialCommitService;

  @PostMapping("/commit")
  public ResponseEntity<ResponseDto<Void>> commit(@RequestHeader("user-id") String userId, @RequestBody MemorialCommitRequestDto dto) {
    memorialCommitService.createMemorialCommit(userId, dto);
    return ResponseEntity.status(201).body(HttpUtil.success("Memorial Commit is successfully created."));
  }
}