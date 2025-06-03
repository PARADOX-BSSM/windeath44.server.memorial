package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.dto.request.MemorialBowRequestDto;
import windeath44.server.memorial.domain.model.MemorialBow;
import windeath44.server.memorial.domain.repository.MemorialBowRepository;

@Service
@RequiredArgsConstructor
public class MemorialBowService {
  private final MemorialBowRepository memorialBowRepository;

  public void bow(MemorialBowRequestDto memorialBowRequestDto) {
    String userId = memorialBowRequestDto.userId();
    Long memorialId = memorialBowRequestDto.memorialId();
    MemorialBow memorialBow = memorialBowRepository.findMemorialBowByUserIdAndMemorialId(userId, memorialId);
    if(memorialBow == null) {
      MemorialBow newMemorialBow = new MemorialBow(
              userId,
              memorialId
      );
      memorialBowRepository.save(newMemorialBow);
    }
    else {
      memorialBow.plusBowCount();
      memorialBowRepository.save(memorialBow);
    }
  }
}
