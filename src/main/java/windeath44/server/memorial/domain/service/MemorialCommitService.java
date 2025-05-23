package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.entity.Memorial;
import windeath44.server.memorial.domain.entity.MemorialCommit;
import windeath44.server.memorial.domain.entity.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialRepository;
import windeath44.server.memorial.domain.exception.*;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.mapper.MemorialCommitMapper;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialCommitResponseDto;

@Service
@RequiredArgsConstructor
public class MemorialCommitService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialCommitMapper memorialCommitMapper;
  private final MemorialRepository memorialRepository;

  public MemorialCommitResponseDto createMemorialCommit(MemorialCommitRequestDto memorialCommitRequestDto) {
    Memorial memorial = memorialRepository.findById(memorialCommitRequestDto.memorialId())
            .orElseThrow(MemorialNotFoundException::new);
    MemorialCommit memorialCommit = memorialCommitRepository.save(memorialCommitMapper.toMemorialCommit(memorialCommitRequestDto, memorial));
    return memorialCommitMapper.toMemorialCommitResponseDto(memorialCommit);
  }

  public MemorialCommitResponseDto findMemorialCommitById(Long memorialCommitId) {
    MemorialCommit memorialCommit = memorialCommitRepository.findById(memorialCommitId).orElseThrow(MemorialCommitNotFoundException::new);
    return memorialCommitMapper.toMemorialCommitResponseDto(memorialCommit);
  }
}
