package windeath44.server.memorial.domain.memorial.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialResponseDto;
import windeath44.server.memorial.domain.memorial.model.event.MemorialTracingEvent;

@Service
@RequiredArgsConstructor
public class MemorialGetServiceCacheProxy {

    private final MemorialGetService memorialGetService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MemorialResponseDto findMemorialById(Long memorialId, String userId) {
        // 캐시된 조회 (이벤트 발행 없음)
        MemorialResponseDto memorial = memorialGetService.findMemorialById(memorialId);

        // 이벤트 발행은 항상 실행
        if (userId != null) {
            applicationEventPublisher.publishEvent(new MemorialTracingEvent(memorialId, userId));
        }

        return memorial;
    }
}
