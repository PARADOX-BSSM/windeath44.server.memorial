package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.avro.MemorialApplicationAvroSchema;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialCommitResponseDto;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;

@Service
@RequiredArgsConstructor
public class MemorialCreateService {
  private final MemorialRepository memorialRepository;
  private final MemorialCommitService memorialCommitService;
  private final MemorialPullRequestService memorialPullRequestService;

  @Transactional
  public Long createMemorial(MemorialApplicationAvroSchema memorialCreationAvroSchema) {
    Memorial memorial = new Memorial(memorialCreationAvroSchema.getCharacterId());
    memorialRepository.save(memorial);
    MemorialCommitRequestDto memorialCommitRequestDto = new MemorialCommitRequestDto(memorial.getMemorialId(), memorialCreationAvroSchema.getContent());

    String applicantId = memorialCreationAvroSchema.getApplicantId();

    MemorialCommitResponseDto memorialCommitResponseDto = memorialCommitService.createMemorialCommit(applicantId, memorialCommitRequestDto);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto = new MemorialPullRequestRequestDto(memorialCommitResponseDto.memorialCommitId());

    String approvedId = memorialCreationAvroSchema.getApproverId();
    memorialPullRequestService.createMemorialPullRequest(approvedId, memorialPullRequestRequestDto);
    return memorial.getMemorialId();
  }
}
