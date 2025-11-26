package windeath44.server.memorial.domain.memorial.repository;

public interface MemorialBowRepositoryCustom {
    Long findCurrentBowRanking(String userId, Long memorialId);
}
