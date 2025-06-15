package windeath44.server.memorial.domain.service.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.dto.response.MemorialCommentResponse;
import windeath44.server.memorial.domain.mapper.MemorialCommentMapper;
import windeath44.server.memorial.domain.model.MemorialComment;
import windeath44.server.memorial.domain.model.MemorialCommentLikesPrimaryKey;
import windeath44.server.memorial.domain.service.MemorialCommentLikesService;
import windeath44.server.memorial.domain.service.MemorialCommentService;
import windeath44.server.memorial.global.dto.CursorPage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemorialCommentGetUseCase {
  private final MemorialCommentService memorialCommentService;
  private final MemorialCommentMapper memorialCommentMapper;
  private final MemorialCommentLikesService memorialCommentLikesService;

  public CursorPage<MemorialCommentResponse> getComment(String userId, Long memorialId, Long cursorId, Integer size) {
    Slice<MemorialComment> memorialRootCommentListSlice = memorialCommentService.getRootComment(memorialId, cursorId, size);

    List<MemorialComment> memorialRootCommentList = memorialRootCommentListSlice.getContent();

    List<Long> rootIds = memorialRootCommentList.stream()
            .map(MemorialComment::getCommentId)
            .toList();
    memorialCommentService.connectChild(memorialRootCommentList, rootIds);
    List<MemorialCommentResponse> memorialCommentResponseList = transformMemorialCommentResponse(userId, memorialRootCommentList);

    return new CursorPage<>(memorialRootCommentListSlice.hasNext(), memorialCommentResponseList);
}

  private List<MemorialCommentResponse> transformMemorialCommentResponse(
          String userId,
          List<MemorialComment> rootComments
  ) {
    Set<Long> allCommentIds = extractAllCommentIds(rootComments);
    Set<Long> likedCommentIds = memorialCommentLikesService.getLikedCommentIds(userId, allCommentIds);

    return rootComments.stream()
            .map(comment -> memorialCommentMapper.toMemorialCommentResponse(comment, likedCommentIds))
            .toList();
  }

  private Set<Long> extractAllCommentIds(List<MemorialComment> comments) {
    Set<Long> ids = new HashSet<>();
    for (MemorialComment comment : comments) {
      ids.add(comment.getCommentId());
      ids.addAll(extractAllCommentIds(comment.getChildren()));
    }
    return ids;
  }

}
