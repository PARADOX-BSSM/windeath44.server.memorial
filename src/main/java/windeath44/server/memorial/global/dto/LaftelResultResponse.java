package windeath44.server.memorial.global.dto;

import windeath44.server.memorial.domain.anime.dto.response.RestAnimeResponse;

import java.util.List;

public record LaftelResultResponse(
        Long count,
        List<RestAnimeResponse> results,
        String next
) {
  public Long getFirstId() {
    return this.results.get(0).id();
  }

  public boolean isEnd() {
    return this.next == null;
  }

  public boolean containsCachedId(Long cachedId) {
    return this.results()
            .stream()
            .anyMatch(anime -> cachedId.equals(anime.id()));
  }
}
