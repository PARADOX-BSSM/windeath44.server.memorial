package windeath44.server.memorial.domain.memorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommitResponseDto;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.memorial.service.MemorialCommitService;

import java.util.List;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialCommitController {
  private final MemorialCommitService memorialCommitService;

  @PostMapping("/commit")
  public ResponseEntity<ResponseDto> commit(@RequestHeader("user-id") String userId, @RequestBody MemorialCommitRequestDto dto) {
    MemorialCommitResponseDto memorialCommitResponseDto = memorialCommitService.createMemorialCommit(userId, dto);
    return ResponseEntity.status(201).body(new ResponseDto("Memorial Commit is successfully created.", memorialCommitResponseDto.memorialCommitId()));
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