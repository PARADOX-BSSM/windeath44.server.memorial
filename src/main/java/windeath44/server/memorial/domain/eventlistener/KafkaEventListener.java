package windeath44.server.memorial.domain.eventlistener;

import com.example.avro.MemorialAvroSchema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import windeath44.server.memorial.domain.service.MemorialCreateService;

@Component
@RequiredArgsConstructor
public class KafkaEventListener {
  private final MemorialCreateService memorialCreateService;

  @KafkaListener(topics = "memorial-creation", groupId = "memorial")
  @Transactional
  public void listen(MemorialAvroSchema memorialAvroSchema) {
    memorialCreateService.createMemorial(memorialAvroSchema);
  }
}
