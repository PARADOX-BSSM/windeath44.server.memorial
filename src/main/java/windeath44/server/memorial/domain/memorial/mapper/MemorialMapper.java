package windeath44.server.memorial.domain.memorial.mapper;

import com.querydsl.core.Tuple;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialResponseDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialSimpleResponseDto;
import windeath44.server.memorial.domain.memorial.model.QMemorial;
import windeath44.server.memorial.domain.memorial.model.QMemorialCommit;
import windeath44.server.memorial.domain.memorial.model.QMemorialPullRequest;
import windeath44.server.memorial.global.dto.OffsetPage;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemorialMapper {
  MemorialMapper INSTANCE = Mappers.getMapper(MemorialMapper.class);

  default MemorialResponseDto toMemorialResponseDto(Tuple tuple,
                                                    @Context QMemorial memorial,
                                                    @Context QMemorialPullRequest memorialPullRequest,
                                                    @Context QMemorialCommit memorialCommit,
                                                    List<String> chiefs) {
    if (tuple == null) return null;
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

  default OffsetPage<MemorialListResponseDto> toMemorialListResponseDto(List<Tuple> tuples,
                                                                         @Context QMemorial memorial,
                                                                         Long total) {
    List<MemorialListResponseDto> memorialList = tuples.stream().map(x -> new MemorialListResponseDto(
            x.get(memorial.memorialId),
            x.get(memorial.characterId)
    )).toList();
    return OffsetPage.of(total.intValue(), memorialList);
  }

  default MemorialSimpleResponseDto toMemorialSimpleResponseDto(
      windeath44.server.memorial.domain.memorial.model.Memorial memorial) {
    if (memorial == null) return null;
    return new MemorialSimpleResponseDto(
        memorial.getMemorialId(),
        memorial.getCharacterId(),
        memorial.getBowCount()
    );
  }
}