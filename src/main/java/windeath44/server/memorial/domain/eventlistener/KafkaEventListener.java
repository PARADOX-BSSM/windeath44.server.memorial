package windeath44.server.memorial.domain.eventlistener;

import com.example.avro.EventResultAvroSchema;
import com.example.avro.MemorialCreationAvroSchema;
import com.example.avro.MemorialDeletionAvroSchema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.service.MemorialCreateService;
import windeath44.server.memorial.domain.service.MemorialDeleteService;

@Component
@RequiredArgsConstructor
public class KafkaEventListener {
  private final MemorialCreateService memorialCreateService;
  private final MemorialDeleteService memorialDeleteService;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @KafkaListener(topics = "memorial-creation-request", groupId = "memorial")
  @Transactional
  public void memorialCreation(MemorialCreationAvroSchema memorialCreationAvroSchema) {
    memorialCreateService.createMemorial(memorialCreationAvroSchema);
    kafkaTemplate.send("memorial-creation-response", new EventResultAvroSchema(true));
  }

  @KafkaListener(topics = "memorial-deletion-request", groupId = "memorial")
  @Transactional
  public void memorialDeletion(MemorialDeletionAvroSchema memorialDeletionAvroSchema) {
    memorialDeleteService.deleteMemorial(memorialDeletionAvroSchema);
    kafkaTemplate.send("memorial-deletion-response", new EventResultAvroSchema(true));
  }
}
