package windeath44.server.memorial.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.dto.ResponseDto;
import windeath44.server.memorial.domain.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialCommitResponseDto;
import windeath44.server.memorial.domain.service.MemorialCommitService;

import java.util.List;

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

  @GetMapping("/commits/{memorialId}")
  public ResponseEntity<ResponseDto> getCommits(@PathVariable Long memorialId) {
    List<MemorialCommitResponseDto> memorialCommitResponseDtos = memorialCommitService.findMemorialCommitsByMemorialId(memorialId);
    return ResponseEntity.status(201).body(new ResponseDto("Memorial Commits are successfully found.", memorialCommitResponseDtos));
  }

  @GetMapping("/commit/{commitId}")
  public ResponseEntity<ResponseDto> getCommit(@PathVariable Long commitId) {
    MemorialCommitResponseDto memorialCommitResponseDto = memorialCommitService.findMemorialCommitById(commitId);
    return ResponseEntity.status(201).body(new ResponseDto("Memorial Commit is successfully found.", memorialCommitResponseDto));
  }
}