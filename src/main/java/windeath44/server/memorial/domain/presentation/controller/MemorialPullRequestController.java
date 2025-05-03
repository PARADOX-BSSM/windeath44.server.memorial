package windeath44.server.memorial.domain.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import windeath44.server.memorial.domain.presentation.dto.ResponseDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.service.MemorialPullRequestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memorials")
public class MemorialPullRequestController {

  private final MemorialPullRequestService memorialPullRequestService;
  @PostMapping("/pull-request")
  public ResponseEntity<ResponseDto> pullRequest(@RequestBody MemorialPullRequestRequestDto dto) {
    memorialPullRequestService.createMemorialPullRequest(dto);
    return ResponseEntity.status(201).body(new ResponseDto("Memorial Commit is successfully requested to pull.", null));
  }
}
