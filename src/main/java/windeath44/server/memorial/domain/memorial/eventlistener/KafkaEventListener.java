package windeath44.server.memorial.domain.memorial.eventlistener;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import windeath44.server.application.avro.MemorialApplicationAvroSchema;
import windeath44.server.memorial.avro.MemorialAvroSchema;
import windeath44.server.memorial.domain.character.service.usecase.MemorializingCharacterUseCase;
import windeath44.server.memorial.domain.memorial.service.MemorialCreateService;

@Component
@RequiredArgsConstructor
public class KafkaEventListener {
  private final MemorialCreateService memorialCreateService;
  private final MemorializingCharacterUseCase memorializingCharacterUseCase;

  private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

  @KafkaListener(topics = "memorial-creation-request", groupId = "memorial")
  @Transactional
  public void memorialCreation(MemorialApplicationAvroSchema memorialApplicationAvroSchema) {
    Long memorialId = memorialCreateService.createMemorial(memorialApplicationAvroSchema);
    Long characterId = memorialApplicationAvroSchema.getCharacterId();
    memorializingCharacterUseCase.memorializing(characterId);
    kafkaTemplate.send("memorial-creation-response", new MemorialAvroSchema(memorialId, memorialApplicationAvroSchema.getApplicantId(), memorialApplicationAvroSchema.getContent(), characterId));
  }
}