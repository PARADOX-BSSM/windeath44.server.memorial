package windeath44.server.memorial.domain.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import windeath44.server.memorial.domain.presentation.dto.global.ResponseDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialRejectDto;

@RestController
@RequestMapping("/memorials")
@RequiredArgsConstructor
public class MemorialRejectController {
//  @PatchMapping("/reject")
//  public ResponseEntity<ResponseDto> reject(@RequestBody MemorialRejectDto)
}
