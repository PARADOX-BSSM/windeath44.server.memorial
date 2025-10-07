package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommentRequestDto;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommentUpdateRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommentCountResponse;
import windeath44.server.memorial.domain.memorial.exception.MemorialCommentNotFoundException;
import windeath44.server.memorial.domain.memorial.mapper.MemorialCommentMapper;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialComment;
import windeath44.server.memorial.domain.memorial.repository.MemorialCommentRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemorialCommentService {
  private final MemorialCommentMapper memorialCommentMapper;
  private final MemorialGetService memorialGetService;
  private final MemorialCommentRepository memorialCommentRepository;

  @Transactional
  public void comment(MemorialCommentRequestDto dto, String userId, Long memorialId) {
    Memorial memorial = memorialGetService.findById(memorialId);

    String content = dto.content();
    Long parentCommentId = dto.parentCommentId();
    MemorialComment parentMemorialComment = parentCommentId == null
            ? null
            : memorialCommentRepository.findById(parentCommentId)
            .orElseThrow(MemorialCommentNotFoundException::new);

    MemorialComment comment = memorialCommentMapper.toMemorialComment(memorial, userId, content, parentMemorialComment);
    memorialCommentRepository.save(comment);
  }

  @Transactional
  public void rewrite(Long commentId, MemorialCommentUpdateRequestDto dto) {
    String content = dto.content();

    MemorialComment comment = memorialCommentRepository.findById(commentId)
            .orElseThrow(MemorialCommentNotFoundException::new);
    comment.rewrite(content);
  }

  @Transactional
  public void delete(Long commentId) {
    MemorialComment memorialComment = memorialCommentRepository.findById(commentId)
                    .orElseThrow(MemorialCommentNotFoundException::new);
    memorialCommentRepository.delete(memorialComment);
  }

  public MemorialComment findCommentById(Long commentId) {
    return memorialCommentRepository.findFetchById(commentId)
            .orElseThrow(MemorialCommentNotFoundException::new);
  }

  public Slice<MemorialComment> getRootComment(Long memorialId, Long cursorId, Integer size) {
    Pageable pageable = PageRequest.of(0, size);
    Slice<MemorialComment> memorialRootCommentSlice = cursorId == null
            ? memorialCommentRepository.findRootComment(memorialId, pageable)
            : memorialCommentRepository.findRootCommentByCursorId(memorialId, cursorId, pageable);
    return memorialRootCommentSlice;
  }

  public void connectChild(List<MemorialComment> memorialRootCommentList, List<Long> rootIds) {
    List<MemorialComment> memorialChildCommentList = memorialCommentRepository.findAllByParentCommentId(rootIds);

    Map<Long, List<MemorialComment>> memorialChildCommentMap = memorialChildCommentList.stream()
            .collect(Collectors.groupingBy(MemorialComment::getParentCommentId));

    for (MemorialComment root : memorialRootCommentList) {
      root.addChild(memorialChildCommentMap.getOrDefault(root.getCommentId(), List.of()));
    }
  }

  public List<MemorialCommentCountResponse> getCommentCountByMemorial(Integer size) {
    Pageable pageable = PageRequest.of(0, size);

    List<MemorialCommentCountResponse> memorialCommentCountResponsesList =  memorialCommentRepository.countCommentsByMemorial(pageable)
            .stream()
            .map(memorialCommentMapper::toMemorialCommentCountResponse)
            .toList();
    return memorialCommentCountResponsesList;
  }
}
