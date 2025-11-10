package windeath44.server.memorial.domain.memorial.model.event;

import windeath44.server.memorial.domain.anime.model.Anime;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialCommit;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;

public record MemorialMergedEvent(
        MemorialPullRequest memorialPullRequest
) {
    public static MemorialMergedEvent of(
            MemorialPullRequest memorialPullRequest
    ) {
        return new MemorialMergedEvent(
                memorialPullRequest
        );
    }
}
