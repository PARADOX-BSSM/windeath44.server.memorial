package windeath44.server.memorial.domain.memorial.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import windeath44.server.memorial.domain.memorial.repository.MemorialBowRepositoryCustom;

import static windeath44.server.memorial.domain.memorial.model.QMemorialBow.memorialBow;

@RequiredArgsConstructor
public class MemorialBowRepositoryImpl implements MemorialBowRepositoryCustom {
        private final JPAQueryFactory queryFactory;

        @Override
        public Long findCurrentBowRanking(String userId, Long memorialId) {
                Long myBowCount = queryFactory
                                .select(memorialBow.bowCount)
                                .from(memorialBow)
                                .where(memorialBow.memorialId.eq(memorialId),
                                                memorialBow.userId.eq(userId))
                                .fetchOne();

                if (myBowCount == null) {
                        return null;
                }

                Long higherCount = queryFactory
                                .select(memorialBow.count())
                                .from(memorialBow)
                                .where(memorialBow.memorialId.eq(memorialId),
                                                memorialBow.bowCount.gt(myBowCount))
                                .fetchOne();

                long higher = higherCount == null ? 0L : higherCount;
                return higher + 1;
        }
}
