package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.domain.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.service.mapper.MemorialCommitMapper;

@Service
@RequiredArgsConstructor
public class MemorialCommitPostService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialCommitMapper memorialCommitMapper;

  public void createMemorialCommit(MemorialCommitRequestDto memorialCommitRequestDto) {
    memorialCommitRepository.save(memorialCommitMapper.toMemorialCommit(memorialCommitRequestDto));
  }
}
