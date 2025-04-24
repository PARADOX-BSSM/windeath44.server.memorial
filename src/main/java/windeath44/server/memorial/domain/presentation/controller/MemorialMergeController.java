package windeath44.server.memorial.domain.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.presentation.dto.ResponseDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialMergeableResponseDto;
import windeath44.server.memorial.domain.service.MemorialMergeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memorials")
public class MemorialMergeController {
  MemorialMergeService memorialMergeService;

  @PostMapping("/mergeable")
  public ResponseEntity<ResponseDto> mergeable(@RequestBody MemorialMergeRequestDto dto) {
    MemorialMergeableResponseDto memorialMergeableResponseDto = memorialMergeService.validateMergeable(dto);
    Boolean mergeable = memorialMergeableResponseDto.mergeable();
    if(mergeable) {
      return ResponseEntity.status(200).body(new ResponseDto("Memorial Pull Request is mergeable.", memorialMergeableResponseDto));
    }
    return ResponseEntity.status(200).body(new ResponseDto("Memorial Pull Request cannot be merged automatically.", memorialMergeableResponseDto));
  }

  @PatchMapping("/merge")
  public ResponseEntity<ResponseDto> merge(@RequestBody MemorialMergeRequestDto dto) {
    memorialMergeService.mergeMemorialCommit(dto);
    return ResponseEntity.status(200).body(new ResponseDto("Memorial Pull Request is successfully merged.", null));
  }

}
