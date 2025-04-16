package windeath44.server.memorial.domain.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialResponseDto;

import java.util.List;

@RestController
@RequestMapping("/memorials")
public class MemorialGetController {
  @GetMapping("")
  public List<MemorialResponseDto> getMemorials() {

  }
  @GetMapping("/{memorial_id}")
  public MemorialResponseDto getMemorial(@PathVariable("memorial_id") Long id) {

  }
}
