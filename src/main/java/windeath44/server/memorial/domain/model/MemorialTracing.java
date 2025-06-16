package windeath44.server.memorial.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "memorial_tracing")
@Builder
@Getter
public class MemorialTracing {
  @Id
  private String memorialTracingId;
  private String userId;
  private Long memorialId;
  @Builder.Default
  private LocalDateTime viewed = LocalDateTime.now();

  public static MemorialTracing of(Long memorialId, String userId) {
    return windeath44.server.memorial.domain.model.MemorialTracing.builder()
            .userId(userId)
            .memorialId(memorialId)
            .build();
  }
}
