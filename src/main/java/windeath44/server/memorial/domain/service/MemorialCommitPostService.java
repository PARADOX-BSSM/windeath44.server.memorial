package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.domain.Memorial;
import windeath44.server.memorial.domain.domain.MemorialCommit;
import windeath44.server.memorial.domain.domain.MemorialCommitState;
import windeath44.server.memorial.domain.domain.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.domain.repository.MemorialRepository;
import windeath44.server.memorial.domain.domain.repository.MemorialUpdateHistoryRepository;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialUpdateHistoryRequestDto;
import windeath44.server.memorial.domain.service.mapper.MemorialCommitMapper;
import windeath44.server.memorial.domain.service.mapper.MemorialUpdateHistoryMapper;

@Service
@RequiredArgsConstructor
public class MemorialCommitPostService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialCommitMapper memorialCommitMapper;
  private final MemorialRepository memorialRepository;
  private final MemorialUpdateHistoryRepository memorialUpdateHistoryRepository;
  private final MemorialUpdateHistoryMapper memorialUpdateHistoryMapper;

  public void createMemorialCommit(MemorialCommitRequestDto memorialCommitRequestDto) {
    Memorial memorial = memorialRepository.findById(memorialCommitRequestDto.memorialId())
            .orElseThrow();
    memorialCommitRepository.save(memorialCommitMapper.toMemorialCommit(memorialCommitRequestDto, memorial));
  }

  public void mergeMemorialCommit(MemorialUpdateHistoryRequestDto memorialUpdateHistoryRequestDto) {
    MemorialCommit memorialCommit = memorialCommitRepository.findById(memorialUpdateHistoryRequestDto.memorialCommitId())
            .orElseThrow();
    MemorialCommit latestMemorialCommit = memorialCommitRepository.findMemorialCommitByMemorialAndState(memorialCommit.getMemorial(), MemorialCommitState.APPROVED);
    memorialCommitRepository.save(updateMemorialCommitState(latestMemorialCommit, MemorialCommitState.STORED));
    memorialCommitRepository.save(updateMemorialCommitState(memorialCommit, MemorialCommitState.APPROVED));
    memorialUpdateHistoryRepository.save(memorialUpdateHistoryMapper.toMemorialUpdateHistory(memorialUpdateHistoryRequestDto, memorialCommit));
  }

  private MemorialCommit updateMemorialCommitState(MemorialCommit memorialCommit, MemorialCommitState state) {
    return new MemorialCommit(
            memorialCommit.getMemorialCommitId(),
            memorialCommit.getUserId(),
            memorialCommit.getMemorial(),
            memorialCommit.getContent(),
            state,
            memorialCommit.getCreatedAt()
    );
  }
}
