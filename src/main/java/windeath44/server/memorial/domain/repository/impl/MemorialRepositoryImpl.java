package windeath44.server.memorial.domain.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import windeath44.server.memorial.domain.model.MemorialPullRequestState;
import windeath44.server.memorial.domain.repository.MemorialRepositoryCustom;
import windeath44.server.memorial.domain.mapper.MemorialMapper;
import windeath44.server.memorial.domain.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.dto.response.MemorialResponseDto;

import java.util.List;
import java.util.Map;

import static windeath44.server.memorial.domain.model.QMemorialPullRequest.memorialPullRequest;
import static windeath44.server.memorial.domain.model.QMemorialCommit.memorialCommit;
import static windeath44.server.memorial.domain.model.QMemorial.memorial;
import static windeath44.server.memorial.domain.model.QMemorialChiefs.memorialChiefs;


@RequiredArgsConstructor
public class MemorialRepositoryImpl implements MemorialRepositoryCustom {
  private final JPAQueryFactory queryFactory;
  private final MemorialMapper memorialMapper;

  @Override
  public MemorialResponseDto findMemorialById(Long memorialId) {
    Tuple result = queryFactory
            .select(memorial.memorialId,
                    memorial.characterId,
                    memorial.bowCount,
                    memorialCommit.memorialCommitId,
                    memorialCommit.content,
                    memorialCommit.userId,
                    memorialCommit.createdAt,
                    memorialPullRequest.userId,
                    memorialPullRequest.updatedAt)
            .from(memorial)
            .where(memorial.memorialId.eq(memorialId))
            .join(memorialPullRequest).on(memorialPullRequest.memorial.memorialId.eq(memorial.memorialId))
            .join(memorialCommit).on(memorialCommit.memorialCommitId.eq(memorialPullRequest.memorialCommit.memorialCommitId))
            .where(
                    memorialPullRequest.state.eq(MemorialPullRequestState.APPROVED)
            )
            .fetchOne();
    List<String> chiefList = queryFactory
            .select(memorialChiefs.userId)
            .from(memorialChiefs)
            .where(memorialChiefs.memorial.memorialId.eq(memorialId))
            .fetch();

    MemorialResponseDto memorialResponseDto = memorialMapper.toMemorialResponseDto(result, memorial, memorialPullRequest, memorialCommit, chiefList);
    return memorialResponseDto;
  }

  @Override
  public List<MemorialListResponseDto> findMemorialsOrderByAndPage(String orderBy, Long page, Long pageSize) {
    Map<String, OrderSpecifier<? extends Comparable<? extends Comparable<?>>>> orderSpecifiers = Map.of(
            "recently-updated", memorialPullRequest.updatedAt.desc(),
            "lately-updated", memorialPullRequest.updatedAt.asc(),
            "ascending-bow-count", memorial.bowCount.asc(),
            "descending-bow-count", memorial.bowCount.desc()
    );
    List<Tuple> result = queryFactory
            .select(memorial.memorialId,
                    memorial.characterId,
                    memorial.bowCount,
                    memorialPullRequest.updatedAt)
            .from(memorial)
            .join(memorialPullRequest).on(memorialPullRequest.memorial.memorialId.eq(memorial.memorialId))
            .join(memorialCommit).on(memorialCommit.memorialCommitId.eq(memorialPullRequest.memorialCommit.memorialCommitId))
            .where(memorialPullRequest.state.eq(MemorialPullRequestState.APPROVED))
            .orderBy(orderSpecifiers.get(orderBy))
            .limit(10).offset((page-1) * pageSize)
            .fetch();
    return memorialMapper.toMemorialListResponseDto(result, memorial);
  }

  @Override
  public List<MemorialListResponseDto> findMemorialsOrderByAndPageCharacterFiltered(String orderBy, Long page, Long pageSize, List<Long> characters) {
    Map<String, OrderSpecifier<? extends Comparable<? extends Comparable<?>>>> orderSpecifiers = Map.of(
            "recently-updated", memorialPullRequest.updatedAt.desc(),
            "lately-updated", memorialPullRequest.updatedAt.asc(),
            "ascending-bow-count", memorial.bowCount.asc(),
            "descending-bow-count", memorial.bowCount.desc()
    );
    List<Tuple> result = queryFactory
            .select(memorial.memorialId,
                    memorial.characterId,
                    memorial.bowCount,
                    memorialPullRequest.updatedAt)
            .from(memorial)
            .join(memorialPullRequest).on(memorialPullRequest.memorial.memorialId.eq(memorial.memorialId))
            .join(memorialCommit).on(memorialCommit.memorialCommitId.eq(memorialPullRequest.memorialCommit.memorialCommitId))
            .where(
                    memorialPullRequest.state.eq(MemorialPullRequestState.APPROVED)
                            .and(memorial.characterId.in(characters))
            )
            .orderBy(orderSpecifiers.get(orderBy))
            .limit(10).offset((page-1) * pageSize)
            .fetch();
    return memorialMapper.toMemorialListResponseDto(result, memorial);
  }
}
