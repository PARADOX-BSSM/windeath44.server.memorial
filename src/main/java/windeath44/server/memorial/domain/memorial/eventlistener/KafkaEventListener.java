package windeath44.server.memorial.domain.memorial.eventlistener;

import com.example.avro.CharacterAvroSchema;
import com.example.avro.MemorialAvroSchema;
import windeath44.server.memorial.avro.MemorialApplicationAvroSchema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.character.service.usecase.MemorializingCharacterUseCase;
import windeath44.server.memorial.domain.memorial.service.MemorialCreateService;
import windeath44.server.memorial.domain.memorial.service.MemorialDeleteService;

@Component
@RequiredArgsConstructor
public class KafkaEventListener {
  private final MemorialCreateService memorialCreateService;
  private final MemorialDeleteService memorialDeleteService;
  private final MemorializingCharacterUseCase memorializingCharacterUseCase;

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @KafkaListener(topics = "memorial-creation-request", groupId = "memorial")
  @Transactional
  public void memorialCreation(MemorialApplicationAvroSchema memorialApplicationAvroSchema) {
    Long memorialId = memorialCreateService.createMemorial(memorialApplicationAvroSchema);
    Long characterId = memorialApplicationAvroSchema.getCharacterId();
    memorializingCharacterUseCase.memorializing(characterId);
    kafkaTemplate.send("memorial-creation-response", new MemorialAvroSchema(memorialId, memorialApplicationAvroSchema.getApplicantId(), memorialApplicationAvroSchema.getContent(), characterId));
  }

  @KafkaListener(topics = "memorial-deletion-request", groupId = "memorial")
  @Transactional
  public void memorialDeletion(CharacterAvroSchema characterAvroSchema) {
    memorialDeleteService.deleteMemorial(characterAvroSchema);
    kafkaTemplate.send("memorial-deletion-response", new MemorialAvroSchema(null, null, null, null)); // 일단 임시로
  }
}