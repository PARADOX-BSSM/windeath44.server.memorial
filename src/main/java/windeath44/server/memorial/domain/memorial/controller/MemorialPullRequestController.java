package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialPullRequestResponseDto;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.memorial.service.MemorialPullRequestService;

import java.util.List;

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

  @GetMapping("/pull-requests/{memorialId}")
  public ResponseEntity<ResponseDto> getPullRequests(@PathVariable Long memorialId) {
    List<MemorialPullRequestResponseDto> memorialPullRequestResponseDtos = memorialPullRequestService.findMemorialPullRequestsByMemorialId(memorialId);
    return ResponseEntity.ok(new ResponseDto("Memorial Pull Requests are successfully found.", memorialPullRequestResponseDtos));
  }

  @GetMapping("/pull-request/{requestId}")
  public ResponseEntity<ResponseDto> getPullRequest(@PathVariable Long requestId) {
    MemorialPullRequestResponseDto memorialPullRequestResponseDto = memorialPullRequestService.findMemorialPullRequestById(requestId);
    return ResponseEntity.ok(new ResponseDto("Memorial Pull Request is successfully found.", memorialPullRequestResponseDto));
  }
}
