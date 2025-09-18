package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.exception.MemorialCommitNotFoundException;
import windeath44.server.memorial.domain.memorial.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialCommit;
import windeath44.server.memorial.domain.memorial.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.memorial.mapper.MemorialCommitMapper;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommitResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemorialCommitService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialCommitMapper memorialCommitMapper;
  private final MemorialRepository memorialRepository;

  @Transactional
  public MemorialCommitResponseDto createMemorialCommit(String userId, MemorialCommitRequestDto memorialCommitRequestDto) {
    Memorial memorial = memorialRepository.findById(memorialCommitRequestDto.memorialId())
            .orElseThrow(MemorialNotFoundException::new);
    MemorialCommit memorialCommit = memorialCommitRepository.save(memorialCommitMapper.toMemorialCommit(userId, memorialCommitRequestDto, memorial));
    return memorialCommitMapper.toMemorialCommitResponseDto(memorialCommit);
  }

  public MemorialCommitResponseDto findMemorialCommitById(Long memorialCommitId) {
    MemorialCommit memorialCommit = memorialCommitRepository.findById(memorialCommitId).orElseThrow(MemorialCommitNotFoundException::new);
    return memorialCommitMapper.toMemorialCommitResponseDto(memorialCommit, memorialCommit.getMemorial());
  }

  public List<MemorialCommitResponseDto> findMemorialCommitsByMemorialId(Long memorialId) {
    List<MemorialCommit> memorialCommits = memorialCommitRepository.findMemorialCommitsByMemorial_MemorialId(memorialId);
    return memorialCommits.stream().map(x -> memorialCommitMapper.toMemorialCommitResponseDto(x, x.getMemorial())).toList();
  }

  @Transactional
  public void deleteMemorialCommitsByMemorialId(Long memorialId) {
    List<MemorialCommit> memorialCommits = memorialCommitRepository.findMemorialCommitsByMemorial_MemorialId(memorialId);
    memorialCommitRepository.deleteAll(memorialCommits);
  }
}
