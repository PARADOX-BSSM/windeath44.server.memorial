package windeath44.server.memorial.domain.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "memorial_tracing")
@Builder
public class MemorialTracing {
  @Id
  private String memorialTracingId;
  private String userId;
  private Long memorialId;

  public static MemorialTracing of(Long memorialId, String userId) {
    return windeath44.server.memorial.domain.model.MemorialTracing.builder()
            .userId(userId)
            .memorialId(memorialId)
            .build();
  }
}
