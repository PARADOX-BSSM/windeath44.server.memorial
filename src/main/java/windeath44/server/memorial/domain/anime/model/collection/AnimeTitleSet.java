package windeath44.server.memorial.domain.anime.model.collection;

import windeath44.server.memorial.domain.anime.dto.response.RestAnimeResponse;
import windeath44.server.memorial.global.dto.LaftelResultResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimeTitleSet {
    private Set<String> cachedTitleSet = new HashSet<>();
    private static final Pattern pattern = Pattern.compile(
            "^\\((?=.*무삭제)?(?=.*(자막|더빙))?.*?\\)\\s*|\\d+기.*$"
    );

    public void addTitleAnimes(LaftelResultResponse animes) {
        animes.results().forEach(anime -> {
            String title = normalizeTitle(anime.name());
            cachedTitleSet.add(title);
        });
    }

    private String normalizeTitle(String title) {
        if (title == null) return "";
        title = title.replaceAll("[\\s\\u200B-\\u200D\\uFEFF\\u00A0]+", "");
        title = pattern.matcher(title).replaceAll(""); // 패턴에 맞는 부분 제거
        return title.trim(); // 앞뒤 공백 제거
    }

    public LaftelResultResponse filter(LaftelResultResponse animes) {
        List<RestAnimeResponse> filterdAnimes = filterAnimes(animes);
        return new LaftelResultResponse(animes.count(), filterdAnimes, animes.next());
    }

    private List<RestAnimeResponse> filterAnimes(LaftelResultResponse animes) {
        return animes.results().stream()
                .map(anime -> {
                    String normTitle = normalizeTitle(anime.name());
                    if (cachedTitleSet.contains(normTitle)) {
                        return null;
                    }
                    return new RestAnimeResponse(
                            anime.id(),
                            normTitle,
                            anime.genres(),
                            anime.img()
                    );
                })
                .filter(Objects::nonNull)
                .toList();
    }

}
