package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.memorial.service.MemorialPullRequestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memorials")
public class MemorialPullRequestController {

  private final MemorialPullRequestService memorialPullRequestService;
  @PostMapping("/pull-request")
  public ResponseEntity<ResponseDto<Void>> pullRequest(@RequestHeader("user-id") String userId, @RequestBody MemorialPullRequestRequestDto dto) {
    memorialPullRequestService.createMemorialPullRequest(userId, dto);
    return ResponseEntity.status(201).body(HttpUtil.success("Memorial Commit is successfully requested to pull."));
  }
}
