package windeath44.server.memorial.domain.anime.service;

import windeath44.server.memorial.domain.anime.dto.response.AnimeResponse;
import windeath44.server.memorial.domain.anime.exception.NotFoundAnimeException;
import windeath44.server.memorial.domain.anime.mapper.AnimeMapper;
import windeath44.server.memorial.domain.anime.model.Anime;
import windeath44.server.memorial.domain.anime.repository.jpa.AnimeRepository;
import windeath44.server.memorial.global.dto.CursorPage;
import windeath44.server.memorial.global.dto.LaftelResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimeService {
  private final AnimeRepository animeRepository;
  private final AnimeMapper animeMapper;

  public CursorPage<AnimeResponse> findAll(Long cursorId, int size, String animeName) {
    return animeName != null ? findAllByName(animeName, cursorId, size) : findAll(cursorId, size);
  }

  private CursorPage<AnimeResponse> findAllByName(String animeName, Long cursorId, int size) {
    Pageable pageable = PageRequest.of(0, size);
    Slice<Anime> animeSlice = cursorId == null
            ? animeRepository.findRecentAnimesByName(pageable, animeName)
            : animeRepository.findRecentAnimesByCursorIdAndName(cursorId, pageable, animeName);
    List<AnimeResponse> animeList = animeMapper.toAnimePageListResponse(animeSlice);
    return new CursorPage<>(animeSlice.hasNext(), animeList);
  }

  private CursorPage<AnimeResponse> findAll(Long cursorId, int size) {
    Pageable pageable = PageRequest.of(0, size);
    Slice<Anime> animeSlice = cursorId == null
            ? animeRepository.findRecentAnimes(pageable)
            : animeRepository.findRecentAnimesByCursorId(cursorId, pageable);
    List<AnimeResponse> animeList = animeMapper.toAnimePageListResponse(animeSlice);
    return new CursorPage<>(animeSlice.hasNext(), animeList);
  }

  private Anime findAnime(Long animeId) {
    Anime anime = animeRepository.findById(animeId)
            .orElseThrow(NotFoundAnimeException::getInstance);
    return anime;
  }

  public Anime getAnime(Long animeId) {
    Anime anime = findAnime(animeId);
    return anime;
  }

  @Transactional(readOnly = false)
  public void delete(Long animeId) {
    Anime anime = findAnime(animeId);
    animeRepository.delete(anime);
  }

  @Transactional(readOnly = false)
  public void save(LaftelResultResponse animeResponse) {
    List<Anime> animeList = animeResponse.results()
            .stream()
            .map(animeMapper::toAnime)
            .toList();
    animeRepository.bulkInsert(animeList);
  }

  public AnimeResponse findById(Long animeId) {
    Anime anime = findAnime(animeId);
    AnimeResponse animeResponse = animeMapper.toAnimeResponse(anime);
    return animeResponse;
  }
}
