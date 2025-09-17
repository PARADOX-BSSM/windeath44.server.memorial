package windeath44.server.memorial.domain.anime.repository;

import windeath44.server.memorial.domain.anime.model.Anime;

import java.util.List;

public interface JdbcAnimeRepository {
  void bulkInsert(List<Anime> animes);
}
