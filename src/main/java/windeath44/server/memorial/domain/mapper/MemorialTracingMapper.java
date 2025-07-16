//package windeath44.server.memorial.domain.mapper;
//
//import org.springframework.stereotype.Component;
//import windeath44.server.memorial.domain.dto.response.MemorialTracingResponse;
//import windeath44.server.memorial.domain.model.MemorialTracing;
//import windeath44.server.memorial.domain.model.event.MemorialTracingEvent;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//
//@Component
//public class MemorialTracingMapper {
//  public MemorialTracing toMemorialTracing(MemorialTracingEvent event) {
//    return MemorialTracing.of(event.memorialId(), event.userId());
//  }
//
//  public MemorialTracingResponse toMemorialTracingResponse(MemorialTracing memorialTracing) {
//    Long memorialId = memorialTracing.getMemorialId();
//    Date viewedAt = memorialTracing.getViewed();
//
//    return MemorialTracingResponse.builder()
//            .memorialId(memorialId)
//            .viewedAt(viewedAt)
//            .build();
//  }
//}
