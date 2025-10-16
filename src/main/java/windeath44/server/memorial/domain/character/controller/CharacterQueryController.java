package windeath44.server.memorial.domain.character.controller;

import windeath44.server.memorial.domain.character.dto.response.CharacterResponse;
import windeath44.server.memorial.domain.character.dto.response.TodayAnniversariesResponse;
import windeath44.server.memorial.domain.character.service.CharacterQueryService;
import windeath44.server.memorial.global.dto.CursorPage;
import windeath44.server.memorial.global.dto.OffsetPage;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animes/characters")
public class CharacterQueryController {
    private final CharacterQueryService characterQueryService;

    @GetMapping
    public ResponseEntity<ResponseDto<CursorPage<CharacterResponse>>> findAll(@RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam int size) {
        CursorPage<CharacterResponse> characterResponses = characterQueryService.findAll(cursorId, size);
        ResponseDto<CursorPage<CharacterResponse>> responseDto = HttpUtil.success("find characters", characterResponses);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{character-id}")
    public ResponseEntity<ResponseDto<CharacterResponse>> findById(@PathVariable("character-id") Long characterId) {
        CharacterResponse characterResponse = characterQueryService.find(characterId);
        ResponseDto<CharacterResponse> responseDto = HttpUtil.success("find character", characterResponse);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search/anime")
    public ResponseEntity<ResponseDto<CursorPage<CharacterResponse>>> findIdsByAnimeId(@RequestParam("animeId") List<Long> animeId, @RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam int size) {
        CursorPage<CharacterResponse> characterResponse = characterQueryService.findByAnime(animeId, size, cursorId);
        ResponseDto<CursorPage<CharacterResponse>> responseDto = HttpUtil.success("find character by anime id", characterResponse);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search/integrated")
    public ResponseEntity<ResponseDto<CursorPage<CharacterResponse>>> findIdsIntegrated(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "animeId", required = false) List<Long> animeId,
            @RequestParam(value = "deathReason", required = false) String deathReason,
            @RequestParam(value = "cursorId", required = false) Long cursorId,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "memorialState", required = false) String memorialState // NOT_MEMORIALIZING, MEMORIALIZING
    ) {
        CursorPage<CharacterResponse> characterResponses = characterQueryService.findAllIntegrated(name, animeId, deathReason, memorialState, cursorId, size);
        ResponseDto<CursorPage<CharacterResponse>> responseDto = HttpUtil.success("find character ids integrated", characterResponses);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search/integrated/offset")
    public ResponseEntity<ResponseDto<OffsetPage<CharacterResponse>>> findIdsIntegratedOffset(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "animeId", required = false) List<Long> animeId,
            @RequestParam(value = "deathReason", required = false) String deathReason,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "memorialState", required = false) String memorialState // NOT_MEMORIALIZING, MEMORIALIZING
    ) {
        OffsetPage<CharacterResponse> characterResponses = characterQueryService.findAllIntegratedOffset(name, animeId, deathReason, memorialState, page, size);
        ResponseDto<OffsetPage<CharacterResponse>> responseDto = HttpUtil.success("find character ids integrated", characterResponses);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search/death-reason")
    public ResponseEntity<ResponseDto<CursorPage<CharacterResponse>>> findIdsByDeathReason(@RequestParam("deathReason") String deathReason, @RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam int size) {
        CursorPage<CharacterResponse> characterResponses = characterQueryService.findAllByDeathReason(deathReason, cursorId, size);
        ResponseDto<CursorPage<CharacterResponse>> responseDto = HttpUtil.success("find character ids by death reason", characterResponses);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search/characterIds")
    public ResponseEntity<ResponseDto<List<CharacterResponse>>> findCharacterResponsesByCharacterIds(@RequestParam List<Long> characterIds) {
        List<CharacterResponse> characterResponseList = characterQueryService.findByCharacterIds(characterIds);
        ResponseDto<List<CharacterResponse>> responseDto = HttpUtil.success("find characters by character ids", characterResponseList);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search/name")
    public ResponseEntity<ResponseDto<CursorPage<CharacterResponse>>> findCharacterResponsesByCharacterName(@RequestParam("name") String name, @RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam int size) {
        CursorPage<CharacterResponse> characterResponses = characterQueryService.findAllByName(name, cursorId, size);
        ResponseDto<CursorPage<CharacterResponse>> responseDto = HttpUtil.success("find characters", characterResponses);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("today-anniversary") 
    public ResponseEntity<ResponseDto<TodayAnniversariesResponse>> findIdsByTodayAnniversaries() {
        TodayAnniversariesResponse todayAnniversariesResponse = characterQueryService.findAllByAnniversaries();
        if (todayAnniversariesResponse.characterIds() == null) {
            return ResponseEntity.status(404).body(HttpUtil.success("characters not found", null));
        }
        ResponseDto<TodayAnniversariesResponse> responseDto = HttpUtil.success("find character ids by anniversary", todayAnniversariesResponse);
        return ResponseEntity.ok(responseDto);
    }
}
