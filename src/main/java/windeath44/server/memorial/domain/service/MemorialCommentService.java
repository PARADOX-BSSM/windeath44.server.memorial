package windeath44.server.memorial.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.dto.request.MemorialCommentRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialCommentResponse;
import windeath44.server.memorial.domain.mapper.MemorialCommentMapper;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.MemorialComment;
import windeath44.server.memorial.domain.repository.MemorialCommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemorialCommentService {
  private final MemorialCommentMapper memorialCommentMapper;
  private final MemorialGetService memorialGetService;
  private final MemorialCommentRepository memorialCommentRepository;

  @Transactional
  public void comment(final MemorialCommentRequestDto dto, final String userId) {
    Long memorialId = dto.memorialId();
    Memorial memorial = memorialGetService.findById(memorialId);

    String content = dto.content();
    Long parentCommentId = dto.parentCommentId();
    MemorialComment comment = memorialCommentMapper.toMemorialComment(memorial, userId, content, parentCommentId);
    memorialCommentRepository.save(comment);
  }

  public List<MemorialCommentResponse> getComment() {
    List<MemorialCommentResponse> memorialCommentResponseList = memorialCommentRepository.findAll()
            .stream()
            .map(memorialCommentMapper::toMemorialCommentResponse)
            .toList();
    return memorialCommentResponseList;
  }
}
