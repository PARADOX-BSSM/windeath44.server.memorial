package windeath44.server.memorial.domain.character.service.usecase;
import windeath44.server.memorial.domain.character.service.CharacterCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemorializingCharacterUseCase {
  private final CharacterCommandService characterCommandService;

  @Transactional
  public void memorializing(Long characterId) {
    characterCommandService.memorializing(characterId);

  }
}
