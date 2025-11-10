package windeath44.server.memorial.domain.character.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import windeath44.server.memorial.domain.character.dto.request.CharacterChangeRequestDto;
import windeath44.server.memorial.domain.character.dto.response.CharacterChangeRequestResponse;
import windeath44.server.memorial.domain.character.service.CharacterChangeRequestCommandService;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animes/characters/change-requests")
public class CharacterChangeRequestController {
    private final CharacterChangeRequestCommandService characterChangeRequestCommandService;

    @PostMapping
    public ResponseEntity<ResponseDto<CharacterChangeRequestResponse>> create(
            @RequestBody @Valid CharacterChangeRequestDto request
    ) {
        CharacterChangeRequestResponse response = characterChangeRequestCommandService.create(request);
        ResponseDto<CharacterChangeRequestResponse> responseDto = HttpUtil.success("create character change request", response);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/memorial-commit/{memorial-commit-id}")
    public ResponseEntity<ResponseDto<CharacterChangeRequestResponse>> getByMemorialCommitId(
            @PathVariable("memorial-commit-id") Long memorialCommitId
    ) {
        CharacterChangeRequestResponse response = characterChangeRequestCommandService.findByMemorialCommitId(memorialCommitId);
        ResponseDto<CharacterChangeRequestResponse> responseDto = HttpUtil.success("get character change request by memorial commit id", response);
        return ResponseEntity.ok(responseDto);
    }
}

