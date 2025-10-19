package windeath44.server.memorial.domain.memorial.service.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommentResponse;
import windeath44.server.memorial.domain.memorial.mapper.MemorialCommentMapper;
import windeath44.server.memorial.domain.memorial.repository.projection.MemorialCommentWithLikeProjection;
import windeath44.server.memorial.domain.memorial.service.MemorialCommentService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemorialPopularCommentGetUseCase {
  private final MemorialCommentService memorialCommentService;
  private final MemorialCommentMapper memorialCommentMapper;

  public List<MemorialCommentResponse> getPopularComment(String userId, Long memorialId, Integer size) {
    List<MemorialCommentWithLikeProjection> projectionList = memorialCommentService.getPopularRootCommentWithLikes(userId, memorialId, size);

    return projectionList.stream()
            .map(memorialCommentMapper::toMemorialCommentResponse)
            .toList();
  }
}
