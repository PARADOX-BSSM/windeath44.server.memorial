package windeath44.server.memorial.domain.character.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import windeath44.server.memorial.domain.anime.model.Anime;
import windeath44.server.memorial.domain.anime.service.AnimeService;
import windeath44.server.memorial.domain.character.exception.NotFoundCharacterChangeRequestException;
import windeath44.server.memorial.domain.character.dto.request.CharacterChangeRequestDto;
import windeath44.server.memorial.domain.character.dto.response.CharacterChangeRequestResponse;
import windeath44.server.memorial.domain.character.mapper.CharacterChangeRequestMapper;
import windeath44.server.memorial.domain.character.model.Character;
import windeath44.server.memorial.domain.character.model.CharacterChangeRequest;
import windeath44.server.memorial.domain.character.repository.CharacterChangeRequestRepository;
import windeath44.server.memorial.domain.memorial.model.MemorialCommit;
import windeath44.server.memorial.domain.memorial.service.MemorialCommitService;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterChangeRequestCommandService {
    private final CharacterChangeRequestRepository characterChangeRequestRepository;
    private final CharacterChangeRequestMapper characterChangeRequestMapper;
    private final CharacterCommandService characterCommandService;
    private final AnimeService animeService;
    private final MemorialCommitService memorialCommitService;

    public CharacterChangeRequestResponse create(CharacterChangeRequestDto dto) {
        Character character = characterCommandService.findById(dto.characterId());
        Anime anime = animeService.getAnime(dto.animeId());
        MemorialCommit memorialCommit = memorialCommitService.findById(dto.memorialCommitId());

        CharacterChangeRequest characterChangeRequest = characterChangeRequestMapper.toCharacterChangeRequest(
                dto, character, anime, memorialCommit
        );

        CharacterChangeRequest saved = characterChangeRequestRepository.save(characterChangeRequest);
        return characterChangeRequestMapper.toCharacterChangeRequestResponse(saved);
    }

    @Transactional(readOnly = true)
    public CharacterChangeRequestResponse findByMemorialCommitId(Long memorialCommitId) {
        CharacterChangeRequest request = characterChangeRequestRepository
                .findByMemorialCommitMemorialCommitId(memorialCommitId)
                .orElseThrow(NotFoundCharacterChangeRequestException::getInstance);
        
        return characterChangeRequestMapper.toCharacterChangeRequestResponse(request);
    }
}

