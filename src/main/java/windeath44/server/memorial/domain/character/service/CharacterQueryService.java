package windeath44.server.memorial.domain.character.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import windeath44.server.memorial.domain.character.dto.response.CharacterResponse;
import windeath44.server.memorial.domain.character.exception.NotFoundCharacterException;
import windeath44.server.memorial.domain.character.mapper.CharacterMapper;
import windeath44.server.memorial.domain.character.model.Character;
import windeath44.server.memorial.domain.character.model.type.CauseOfDeath;
import windeath44.server.memorial.domain.character.model.type.CharacterState;
import windeath44.server.memorial.domain.character.repository.CharacterRepository;
import windeath44.server.memorial.global.dto.CursorPage;
import windeath44.server.memorial.global.dto.OffsetPage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterQueryService {
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;

    public windeath44.server.memorial.domain.character.model.Character findById(Long characterId) {
        windeath44.server.memorial.domain.character.model.Character character = findCharacterById(characterId);
        return character;
    }

    public windeath44.server.memorial.domain.character.model.Character findCharacterById(Long characterId) {
        windeath44.server.memorial.domain.character.model.Character character = characterRepository.findById(characterId)
                .orElseThrow(NotFoundCharacterException::getInstance);
        return character;
    }

    public CursorPage<CharacterResponse> findAll(Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);

        Slice<windeath44.server.memorial.domain.character.model.Character> characterSlice = cursorId == null
                ? characterRepository.findAllPageable(pageable)
                :  characterRepository.findAllByCursorId(cursorId, pageable);
        List<CharacterResponse> characterList = characterMapper.toCharacterListResponse(characterSlice);
        return new CursorPage<>(characterSlice.hasNext(), characterList);
    }


    public CharacterResponse find(Long characterId) {
        windeath44.server.memorial.domain.character.model.Character character = findCharacterById(characterId);
        CharacterResponse characterResponse = characterMapper.toCharacterResponse(character);
        return characterResponse;
    }

    public CursorPage<CharacterResponse> findByAnime(List<Long> animeId, int size, Long cursorId) {
        Pageable pageable = PageRequest.of(0, size);

        Slice<windeath44.server.memorial.domain.character.model.Character> characterList = cursorId == null
                ? characterRepository.findByAnimeId(animeId, pageable)
                :  characterRepository.findByAnimeIdAndCursorId(animeId, cursorId, pageable);
        List<CharacterResponse> characterResponseList = characterMapper.toCharacterListResponse(characterList);
        return new CursorPage<>(characterList.hasNext(), characterResponseList);
    }

    public List<Long> findIdsByDeathReason(String deathReason, Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);

        List<Long> characterIds = cursorId == null
                ? characterRepository.findIdsByDeathReason(deathReason, pageable)
                :  characterRepository.findIdsByDeathReasonAndCursorId(deathReason, cursorId, pageable);
        return characterIds;
    }

    public List<CharacterResponse> findByCharacterIds(List<Long> characterIds) {
        List<CharacterResponse> characterList = characterRepository.findAllByIds(characterIds)
                .stream()
                .map(characterMapper::toCharacterResponse)
                .toList();
        return characterList;
    }

    public CursorPage<CharacterResponse> findAllByName(String name, Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<windeath44.server.memorial.domain.character.model.Character> characterSlice = cursorId == null
                ? characterRepository.findAllPageableByName(name, pageable)
                : characterRepository.findAllByCursorIdAndName(name, cursorId, pageable);
        List<CharacterResponse> characterList = characterMapper.toCharacterListResponse(characterSlice);
        return new CursorPage<>(characterSlice.hasNext(), characterList);
    }

    public CursorPage<CharacterResponse> findAllByDeathReason(String deathReason, Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);
        CauseOfDeath causeOfDeath = CauseOfDeath.valueOfDeathReason(deathReason);

        Slice<windeath44.server.memorial.domain.character.model.Character> characterSlice = cursorId == null
                ? characterRepository.findAllPageableByDeathReason(causeOfDeath, pageable)
                : characterRepository.findAllByCursorIdAndDeathReason(causeOfDeath, cursorId, pageable);
        List<CharacterResponse> characterList = characterMapper.toCharacterListResponse(characterSlice);
        return new CursorPage<>(characterSlice.hasNext(), characterList);
    }

    @Cacheable(
            cacheNames = "characterIntegrated",
            key = "'search:' + #name + ':' + #animeId + ':' + #deathReason + ':' + #memorialState + ':' + #cursorId + ':' + #size"
    )
    public CursorPage<CharacterResponse> findAllIntegrated(String name, List<Long> animeId, String deathReason, String memorialState, Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);

        // 사인 변환
        boolean isNotNullDeathOfReason = deathReason != null;
        CauseOfDeath causeOfDeath = isNotNullDeathOfReason ? CauseOfDeath.valueOfDeathReason(deathReason) : null;

        // 캐릭터 추모 상태 변환
        boolean isNotNullMemorialState = memorialState != null;
        CharacterState characterState = isNotNullMemorialState ? CharacterState.valueOf(memorialState) : null;

        List<Long> animeIds = (animeId == null || animeId.isEmpty()) ? null : animeId;
        Slice<Character> characterSlice = characterRepository.findAllWithCursor(name, animeIds, causeOfDeath, characterState, cursorId, pageable);

        System.out.println(characterSlice.toString());
        List<CharacterResponse> characterList = characterMapper.toCharacterListResponse(characterSlice);
        return new CursorPage<>(characterSlice.hasNext(), characterList);
    }

    public OffsetPage<CharacterResponse> findAllIntegratedOffset(String name, List<Long> animeId, String deathReason, String memorialState, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // 사인 변환
        boolean isNotNullDeathOfReason = deathReason != null;
        CauseOfDeath causeOfDeath = isNotNullDeathOfReason ? CauseOfDeath.valueOfDeathReason(deathReason) : null;

        // 캐릭터 추모 상태 변환
        boolean isNotNullMemorialState = memorialState != null;
        CharacterState characterState = isNotNullMemorialState ? CharacterState.valueOf(memorialState) : null;

        List<Long> animeIds = (animeId == null || animeId.isEmpty()) ? null : animeId;
        org.springframework.data.domain.Page<Character> characterPage = characterRepository.findAllWithOffset(name, animeIds, causeOfDeath, characterState, pageable);

        List<CharacterResponse> characterList = characterMapper.toCharacterListResponse(characterPage);
        return new OffsetPage<>((int) characterPage.getTotalElements(), characterList);
    }

}
