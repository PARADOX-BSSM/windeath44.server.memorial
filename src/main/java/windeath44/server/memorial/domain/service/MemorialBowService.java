package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.dto.request.MemorialBowRequestDto;
import windeath44.server.memorial.domain.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.MemorialBow;
import windeath44.server.memorial.domain.repository.MemorialBowRepository;
import windeath44.server.memorial.domain.repository.MemorialRepository;

@Service
@RequiredArgsConstructor
public class MemorialBowService {
  private final MemorialBowRepository memorialBowRepository;
  private final MemorialRepository memorialRepository;

  public void bow(MemorialBowRequestDto memorialBowRequestDto) {
    String userId = memorialBowRequestDto.userId();
    Long memorialId = memorialBowRequestDto.memorialId();
    Memorial memorial = memorialRepository.findById(memorialId).orElse(null);
    if (memorial == null) {
      throw new MemorialNotFoundException();
    }
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

  public Long BowCountByMemorialId(Long memorialId) {
    Memorial memorial = memorialRepository.findById(memorialId).orElse(null);
    if(memorial == null) {
      throw new MemorialNotFoundException();
    }
    Long bowCount = memorialBowRepository.sumBowCount(memorialId);
    return bowCount == null ? 0 : bowCount;
  }
}
