package windeath44.server.memorial.domain.memorial.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import windeath44.server.memorial.domain.memorial.mapper.MemorialMapperImpl;
import windeath44.server.memorial.domain.memorial.model.MemorialBow;
import windeath44.server.memorial.global.config.QueryDslConfig;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import({ QueryDslConfig.class, MemorialMapperImpl.class })
class MemorialBowRepositoryImplTest {

    @Autowired
    private MemorialBowRepository memorialBowRepository;

    @DisplayName("bowCount 기준으로 dense rank를 반환한다")
    @Test
    void findCurrentBowRanking_denseRank() {
        Long memorialId = 1L;
        saveBow(memorialId, "user-1", 5);
        saveBow(memorialId, "user-2", 5); // 동률 -> 같은 1위
        saveBow(memorialId, "user-3", 3);
        saveBow(memorialId, "user-4", 1);

        Long rankUser1 = memorialBowRepository.findCurrentBowRanking("user-1", memorialId);
        Long rankUser2 = memorialBowRepository.findCurrentBowRanking("user-2", memorialId);
        Long rankUser3 = memorialBowRepository.findCurrentBowRanking("user-3", memorialId);
        Long rankUser4 = memorialBowRepository.findCurrentBowRanking("user-4", memorialId);

        assertThat(rankUser1).isEqualTo(1L);
        assertThat(rankUser2).isEqualTo(1L); // 동률 -> 동일 순위
        assertThat(rankUser3).isEqualTo(3L);
        assertThat(rankUser4).isEqualTo(4L);
    }

    @DisplayName("해당 memorial/user 데이터가 없으면 null을 반환한다")
    @Test
    void findCurrentBowRanking_returnsNullWhenAbsent() {
        Long memorialId = 2L;
        saveBow(memorialId, "existing-user", 2);

        Long result = memorialBowRepository.findCurrentBowRanking("missing-user", memorialId);

        assertThat(result).isNull();
    }

    private void saveBow(Long memorialId, String userId, int bowCount) {
        MemorialBow bow = new MemorialBow(userId, memorialId);
        for (int i = 1; i < bowCount; i++) {
            bow.plusBowCount();
        }
        memorialBowRepository.save(bow);
    }
}
