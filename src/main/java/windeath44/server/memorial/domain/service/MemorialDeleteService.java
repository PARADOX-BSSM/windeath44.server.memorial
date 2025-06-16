package windeath44.server.memorial.domain.service;

import com.example.avro.MemorialDeletionAvroSchema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.repository.MemorialRepository;

@Service
@RequiredArgsConstructor
public class MemorialDeleteService {
  private final MemorialRepository memorialRepository;
  private final MemorialCommitService memorialCommitService;
  private final MemorialPullRequestService memorialPullRequestService;

  @Transactional
  public void deleteMemorial(MemorialDeletionAvroSchema memorialDeletionAvroSchema) {
    Memorial memorial = memorialRepository.findMemorialByCharacterId(memorialDeletionAvroSchema.getCharacterId());
    if (memorial != null) {
      memorialCommitService.deleteMemorialCommitsByMemorialId(memorial.getMemorialId());
      memorialPullRequestService.deleteMemorialPullRequestsByMemorialId(memorial.getMemorialId());
      memorialRepository.delete(memorial);
    }
  }
}
