package windeath44.server.memorial.domain.service;

import com.example.avro.MemorialAvroSchema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialCommitResponseDto;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.repository.MemorialRepository;

@Service
@RequiredArgsConstructor
public class MemorialCreateService {
  private final MemorialRepository memorialRepository;
  private final MemorialCommitService memorialCommitService;
  private final MemorialPullRequestService memorialPullRequestService;

  @Transactional
  public void createMemorial(MemorialAvroSchema memorialAvroSchema) {
    Memorial memorial = new Memorial(memorialAvroSchema.getCharacterId());
    memorialRepository.save(memorial);
    MemorialCommitRequestDto memorialCommitRequestDto = new MemorialCommitRequestDto(memorialAvroSchema.getApplicantId(), memorial.getMemorialId(), memorialAvroSchema.getContent());
    MemorialCommitResponseDto memorialCommitResponseDto = memorialCommitService.createMemorialCommit(memorialCommitRequestDto);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto = new MemorialPullRequestRequestDto(memorialAvroSchema.getApproverId(), memorialCommitResponseDto.memorialCommitId());
    memorialPullRequestService.createMemorialPullRequest(memorialPullRequestRequestDto);
  }
}
