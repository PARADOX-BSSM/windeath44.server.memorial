package windeath44.server.memorial.domain.memorial.service.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommentResponse;
import windeath44.server.memorial.domain.memorial.mapper.MemorialCommentMapper;
import windeath44.server.memorial.domain.memorial.repository.projection.MemorialCommentWithLikeProjection;
import windeath44.server.memorial.domain.memorial.service.MemorialCommentService;
import windeath44.server.memorial.global.dto.CursorPage;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemorialCommentGetUseCase {
  private final MemorialCommentService memorialCommentService;
  private final MemorialCommentMapper memorialCommentMapper;

  public CursorPage<MemorialCommentResponse> getComment(String userId, Long memorialId, Long cursorId, Integer size) {
    Slice<MemorialCommentWithLikeProjection> projectionSlice = memorialCommentService.getRootCommentWithLikes(userId, memorialId, cursorId, size);

    List<MemorialCommentResponse> memorialCommentResponseList = projectionSlice.getContent()
            .stream()
            .map(memorialCommentMapper::toMemorialCommentResponse)
            .toList();

    return new CursorPage<>(projectionSlice.hasNext(), memorialCommentResponseList);
  }

}
