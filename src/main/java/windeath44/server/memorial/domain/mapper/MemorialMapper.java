package windeath44.server.memorial.domain.mapper;

import com.querydsl.core.Tuple;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import windeath44.server.memorial.domain.entity.QMemorial;
import windeath44.server.memorial.domain.entity.QMemorialCommit;
import windeath44.server.memorial.domain.entity.QMemorialPullRequest;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialResponseDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemorialMapper {
  default MemorialResponseDto toMemorialResponseDto(Tuple tuple,
                                                    @Context QMemorial memorial,
                                                    @Context QMemorialPullRequest memorialPullRequest,
                                                    @Context QMemorialCommit memorialCommit,
                                                    List<String> chiefs) {
    if (tuple == null) return null;
    System.out.println(chiefs);
    return new MemorialResponseDto(
            tuple.get(memorial.memorialId),
            tuple.get(memorial.characterId),
            chiefs,
            tuple.get(memorial.bowCount),
            tuple.get(memorialCommit.memorialCommitId),
            tuple.get(memorialCommit.content),
            tuple.get(memorialCommit.userId),
            tuple.get(memorialCommit.createdAt),
            tuple.get(memorialPullRequest.userId),
            tuple.get(memorialPullRequest.updatedAt)
    );
  };
}
