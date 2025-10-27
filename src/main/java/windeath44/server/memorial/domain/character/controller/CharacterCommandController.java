package windeath44.server.memorial.domain.character.controller;

import windeath44.server.memorial.domain.character.dto.request.CharacterRequest;
import windeath44.server.memorial.domain.character.dto.response.CharacterIdResponse;
import windeath44.server.memorial.domain.character.service.CharacterCommandService;
import windeath44.server.memorial.domain.character.service.usecase.CharacterImageUploadUseCase;
import windeath44.server.memorial.domain.character.service.usecase.CreateCharacterUseCase;
import windeath44.server.memorial.global.dto.FileUploadUrlResponse;
import windeath44.server.memorial.global.dto.ResponseDto;
import windeath44.server.memorial.global.util.HttpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animes/characters")
public class CharacterCommandController {
  private final CharacterCommandService characterCommandService;

  private final CreateCharacterUseCase createCharacterUseCase;
  private final CharacterImageUploadUseCase characterImageUploadUseCase;

  @PostMapping
  public ResponseEntity<ResponseDto<CharacterIdResponse>> create(@RequestBody @Valid CharacterRequest characterRequest) {
    CharacterIdResponse characterId = createCharacterUseCase.execute(characterRequest);
    ResponseDto<CharacterIdResponse> responseDto = HttpUtil.success("create character", characterId);
    return ResponseEntity.ok(responseDto);
  }

  @PostMapping(value="/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResponseDto<FileUploadUrlResponse>> uploadImage(@RequestHeader("user-id") String userId, @RequestParam("image") MultipartFile image) {
    FileUploadUrlResponse characterImageUrlResponse = characterImageUploadUseCase.upload(userId, image);
    ResponseDto<FileUploadUrlResponse> responseDto = HttpUtil.success("upload image", characterImageUrlResponse);
    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping("/{character-id}")
  public ResponseEntity<ResponseDto<Void>> delete(@PathVariable("character-id") Long characterId) {
    characterCommandService.deleteById(characterId);
    ResponseDto<Void> responseDto = HttpUtil.success("delete character by id");
    return ResponseEntity.ok(responseDto);
  }

  @PatchMapping("/{character-id}")
  public ResponseEntity<ResponseDto<Void>> update(@PathVariable("character-id") Long characterId, @RequestBody @Valid CharacterRequest characterUpdateRequest) {
    characterCommandService.update(characterUpdateRequest, characterId);
    ResponseDto<Void> responseDto = HttpUtil.success("update character by id");
    return ResponseEntity.ok(responseDto);
  }

}
