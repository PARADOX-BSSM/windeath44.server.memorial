package windeath44.server.memorial.domain.memorial.service;

import com.example.avro.CharacterAvroSchema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;

@Service
@RequiredArgsConstructor
public class MemorialDeleteService {
  private final MemorialRepository memorialRepository;
  private final MemorialCommitService memorialCommitService;
  private final MemorialPullRequestService memorialPullRequestService;

  @Transactional
  public void deleteMemorial(CharacterAvroSchema characterAvroSchema) {
    Memorial memorial = memorialRepository.findMemorialByCharacterId(characterAvroSchema.getCharacterId());
    if (memorial != null) {
      memorialCommitService.deleteMemorialCommitsByMemorialId(memorial.getMemorialId());
      memorialPullRequestService.deleteMemorialPullRequestsByMemorialId(memorial.getMemorialId());
      memorialRepository.delete(memorial);
    }
  }
}
