package windeath44.server.memorial.domain.entity.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import windeath44.server.memorial.domain.entity.MemorialPullRequestState;
import windeath44.server.memorial.domain.entity.repository.MemorialRepositoryCustom;
import windeath44.server.memorial.domain.mapper.MemorialMapper;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialResponseDto;

import java.util.List;

import static windeath44.server.memorial.domain.entity.QMemorial.memorial;
import static windeath44.server.memorial.domain.entity.QMemorialPullRequest.memorialPullRequest;
import static windeath44.server.memorial.domain.entity.QMemorialCommit.memorialCommit;

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
    StringPath chief = Expressions.stringPath("chief");
    List<String> chiefList = queryFactory
            .select(chief)
            .from(memorial)
            .join(memorial.chiefs, chief)
            .where(memorial.memorialId.eq(memorialId))
            .fetch();

//     select memorial.memorial_id,
//                        memorial.character_id,
//                        memorial.chiefs,
//                        memorial.bow_count,
//                        memorialCommit.memorial_commit_id,
//                        memorialCommit.content,
//                        memorialCommit.user_id,
//                        memorialCommit.created_at,
//                        memorialPullRequest.user_id,
//                        memorialPullRequest.updated_at
//                        from memorial
//                        join memorial_pull_request as memorialPullRequest on memorialPullRequest.memorial_id = memorial.memorial_id
//                        join memorial_commit as memorialCommit on memorialCommit.memorial_commit_id = memorialPullRequest.memorial_commit_id
//                        where memorialPullRequest.state = 'APPROVED';

    MemorialResponseDto memorialResponseDto = memorialMapper.toMemorialResponseDto(result, memorial, memorialPullRequest, memorialCommit, chiefList);
    return memorialResponseDto;
  }
}
