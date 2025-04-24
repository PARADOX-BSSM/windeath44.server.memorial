package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.entity.Memorial;
import windeath44.server.memorial.domain.entity.MemorialCommit;
import windeath44.server.memorial.domain.entity.MemorialPullRequestState;
import windeath44.server.memorial.domain.entity.MemorialPullRequest;
import windeath44.server.memorial.domain.entity.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.exception.*;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestDto;
import windeath44.server.memorial.domain.mapper.MemorialCommitMapper;
import windeath44.server.memorial.domain.presentation.dto.response.CompareContentsResponseDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialMergeableResponseDto;
import windeath44.server.memorial.global.diff_match_patch;
import windeath44.server.memorial.global.diff_match_patch.Diff;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class MemorialCommitService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialCommitMapper memorialCommitMapper;
  private final MemorialRepository memorialRepository;

  public void createMemorialCommit(MemorialCommitRequestDto memorialCommitRequestDto) {
    Memorial memorial = memorialRepository.findById(memorialCommitRequestDto.memorialId())
            .orElseThrow(MemorialNotFoundException::new);
    memorialCommitRepository.save(memorialCommitMapper.toMemorialCommit(memorialCommitRequestDto, memorial));
  }
}
