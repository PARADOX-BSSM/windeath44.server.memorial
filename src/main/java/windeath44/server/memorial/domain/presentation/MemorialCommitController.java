package windeath44.server.memorial.domain.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.presentation.dto.ResponseDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialMergeableResponseDto;
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

  @PostMapping("/pull-request")
  public ResponseEntity<ResponseDto> pullRequest(@RequestBody MemorialPullRequestDto dto) {
    memorialCommitService.createMemorialPullRequest(dto);
    return ResponseEntity.status(201).body(new ResponseDto("Memorial Commit is successfully requested to pull.", null));
  }

  @PostMapping("/mergeable")
  public ResponseEntity<ResponseDto> mergeable(@RequestBody MemorialMergeRequestDto dto) {
    MemorialMergeableResponseDto memorialMergeableResponseDto = memorialCommitService.validateMergeable(dto);
    Boolean mergeable = memorialMergeableResponseDto.mergeable();
    if(mergeable) {
      return ResponseEntity.status(200).body(new ResponseDto("Memorial Pull Request is mergeable.", memorialMergeableResponseDto));
    }
    return ResponseEntity.status(200).body(new ResponseDto("Memorial Pull Request cannot be merged automatically.", memorialMergeableResponseDto));
  }

  @PatchMapping("/merge")
  public ResponseEntity<ResponseDto> merge(@RequestBody MemorialMergeRequestDto dto) {
    memorialCommitService.mergeMemorialCommit(dto);
    return ResponseEntity.status(200).body(new ResponseDto("Memorial Pull Request is successfully merged.", null));
  }
}