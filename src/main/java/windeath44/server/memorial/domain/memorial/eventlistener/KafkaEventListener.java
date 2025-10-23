package windeath44.server.memorial.domain.memorial.eventlistener;

import org.apache.avro.specific.SpecificRecordBase;
import com.example.avro.CharacterAvroSchema;
import com.example.avro.MemorialAvroSchema;
import com.example.avro.MemorialApplicationAvroSchema;
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