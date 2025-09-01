package windeath44.server.memorial.domain.anime.controller;
import windeath44.server.memorial.domain.anime.dto.response.AnimeResponse;
import windeath44.server.memorial.domain.anime.service.AnimeService;
import windeath44.server.memorial.global.dto.CursorPage;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animes")
@RequiredArgsConstructor
public class AnimeController {
  private final AnimeService animeService;

  @GetMapping
  public ResponseEntity<ResponseDto<CursorPage<AnimeResponse>>> findAll(@RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam("size") int size, @RequestParam(value = "animeName", required = false) String animeName) {
    CursorPage<AnimeResponse> animeList = animeService.findAll(cursorId, size, animeName);
    ResponseDto<CursorPage<AnimeResponse>> responseDto = HttpUtil.success("find animes", animeList);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{anime-id}")
  public ResponseEntity<ResponseDto<AnimeResponse>> findById(@PathVariable("anime-id") Long animeId) {
    AnimeResponse anime = animeService.findById(animeId);
    ResponseDto<AnimeResponse> responseDto = HttpUtil.success("find anime", anime);
    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping("/{anime-id}")
  public ResponseEntity<ResponseDto<Void>> delete(@PathVariable("anime-id") Long animeId) {
    animeService.delete(animeId);
    ResponseDto<Void> responseDto = HttpUtil.success("delete anime");
    return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(responseDto);
  }
}
