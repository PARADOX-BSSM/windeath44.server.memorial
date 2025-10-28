package windeath44.server.memorial.domain.memorial.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "user_memorial_tracing")
@Builder
@Getter
public class MemorialTracing {
  @Id
  private String memorialTracingId;
  private String userId;
  private Long memorialId;
  @Builder.Default
  private Date viewed = new Date();
  @Builder.Default
  private Integer durationSeconds = 0;

  public static MemorialTracing of(Long memorialId, String userId) {
    return MemorialTracing.builder()
            .userId(userId)
            .memorialId(memorialId)
            .build();
  }

  public boolean checkOwner(String userId) {
    return this.userId.equals(userId);
  }
}
