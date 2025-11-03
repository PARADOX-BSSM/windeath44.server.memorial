package windeath44.server.memorial.domain.memorial.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequestState;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepositoryCustom;
import windeath44.server.memorial.domain.memorial.mapper.MemorialMapper;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialResponseDto;
import windeath44.server.memorial.global.dto.OffsetPage;

import java.util.List;
import java.util.Map;

import static windeath44.server.memorial.domain.memorial.model.QMemorialPullRequest.memorialPullRequest;
import static windeath44.server.memorial.domain.memorial.model.QMemorialCommit.memorialCommit;
import static windeath44.server.memorial.domain.memorial.model.QMemorial.memorial;
import static windeath44.server.memorial.domain.memorial.model.QMemorialChiefs.memorialChiefs;

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
            .join(memorialCommit).on(memorialCommit.memorialCommitId.eq(memorialPullRequest.toCommit.memorialCommitId))
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
  public OffsetPage<MemorialListResponseDto> findMemorialsOrderByAndPage(String orderBy, Long page, Long pageSize) {
    Map<String, OrderSpecifier<? extends Comparable<? extends Comparable<?>>>> orderSpecifiers = Map.of(
            "recently-updated", memorialPullRequest.updatedAt.desc(),
            "lately-updated", memorialPullRequest.updatedAt.asc(),
            "ascending-bow-count", memorial.bowCount.asc(),
            "descending-bow-count", memorial.bowCount.desc()
    );
    Long total = queryFactory
            .select(memorial.count())
            .from(memorial)
            .join(memorialPullRequest).on(memorialPullRequest.memorial.memorialId.eq(memorial.memorialId))
            .join(memorialCommit).on(memorialCommit.memorialCommitId.eq(memorialPullRequest.toCommit.memorialCommitId))
            .where(memorialPullRequest.state.eq(MemorialPullRequestState.APPROVED))
            .fetchOne();
    List<Tuple> result = queryFactory
            .select(memorial.memorialId,
                    memorial.characterId,
                    memorial.bowCount,
                    memorialPullRequest.updatedAt)
            .from(memorial)
            .join(memorialPullRequest).on(memorialPullRequest.memorial.memorialId.eq(memorial.memorialId))
            .join(memorialCommit).on(memorialCommit.memorialCommitId.eq(memorialPullRequest.toCommit.memorialCommitId))
            .where(memorialPullRequest.state.eq(MemorialPullRequestState.APPROVED))
            .orderBy(orderSpecifiers.get(orderBy))
            .limit(10).offset((page-1) * pageSize)
            .fetch();
    return memorialMapper.toMemorialListResponseDto(result, memorial, total);
  }

  @Override
  public OffsetPage<MemorialListResponseDto> findMemorialsOrderByAndPageCharacterFiltered(String orderBy, Long page, Long pageSize, List<Long> characters) {
    Map<String, OrderSpecifier<? extends Comparable<? extends Comparable<?>>>> orderSpecifiers = Map.of(
            "recently-updated", memorialPullRequest.updatedAt.desc(),
            "lately-updated", memorialPullRequest.updatedAt.asc(),
            "ascending-bow-count", memorial.bowCount.asc(),
            "descending-bow-count", memorial.bowCount.desc()
    );
    Long total = queryFactory
            .select(memorial.count())
            .from(memorial)
            .join(memorialPullRequest).on(memorialPullRequest.memorial.memorialId.eq(memorial.memorialId))
            .join(memorialCommit).on(memorialCommit.memorialCommitId.eq(memorialPullRequest.toCommit.memorialCommitId))
            .where(
                    memorialPullRequest.state.eq(MemorialPullRequestState.APPROVED)
                            .and(memorial.characterId.in(characters))
            )
            .fetchOne();
    List<Tuple> result = queryFactory
            .select(memorial.memorialId,
                    memorial.characterId,
                    memorial.bowCount,
                    memorialPullRequest.updatedAt)
            .from(memorial)
            .join(memorialPullRequest).on(memorialPullRequest.memorial.memorialId.eq(memorial.memorialId))
            .join(memorialCommit).on(memorialCommit.memorialCommitId.eq(memorialPullRequest.toCommit.memorialCommitId))
            .where(
                    memorialPullRequest.state.eq(MemorialPullRequestState.APPROVED)
                            .and(memorial.characterId.in(characters))
            )
            .orderBy(orderSpecifiers.get(orderBy))
            .limit(10).offset((page-1) * pageSize)
            .fetch();
    return memorialMapper.toMemorialListResponseDto(result, memorial, total);
  }

  @Override
  public List<MemorialResponseDto> findByIds(List<Long> memorialIds) {
    List<Tuple> results = queryFactory
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
            .where(memorial.memorialId.in(memorialIds))
            .join(memorialPullRequest).on(memorialPullRequest.memorial.memorialId.eq(memorial.memorialId))
            .join(memorialCommit).on(memorialCommit.memorialCommitId.eq(memorialPullRequest.toCommit.memorialCommitId))
            .where(
                    memorialPullRequest.state.eq(MemorialPullRequestState.APPROVED)
            )
            .fetch();

    return results.stream()
            .map(result -> {
                Long memorialId = result.get(memorial.memorialId);
                List<String> chiefList = queryFactory
                        .select(memorialChiefs.userId)
                        .from(memorialChiefs)
                        .where(memorialChiefs.memorial.memorialId.eq(memorialId))
                        .fetch();
                return memorialMapper.toMemorialResponseDto(result, memorial, memorialPullRequest, memorialCommit, chiefList);
            })
            .toList();
  }
}
