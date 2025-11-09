package windeath44.server.memorial.domain.character.service.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.character.exception.NotFoundCharacterException;
import windeath44.server.memorial.domain.character.model.Character;
import windeath44.server.memorial.domain.character.repository.CharacterRepository;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;
import windeath44.server.memorial.domain.memorial.model.event.MemorialMergedEvent;

@Component
@RequiredArgsConstructor
public class CharacterUpdateUseCase {
    private final CharacterRepository characterRepository;

    @EventListener
    @Transactional
    public void updateCharacterInfo(MemorialMergedEvent event) {
        MemorialPullRequest memorialPullRequest = event.memorialPullRequest();
        if (!memorialPullRequest.isChangedCharacter()) return;

        Long characterId = memorialPullRequest.getCharacterId();
        Character character = characterRepository.findById(characterId)
                .orElseThrow(NotFoundCharacterException::getInstance);

        character.update(memorialPullRequest.getCharacterUpdateRequest());

    }
}
