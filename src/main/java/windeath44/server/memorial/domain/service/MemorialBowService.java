package windeath44.server.memorial.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.dto.request.MemorialBowRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialBowResponseDto;
import windeath44.server.memorial.domain.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.mapper.MemorialBowMapper;
import windeath44.server.memorial.domain.model.MemorialBow;
import windeath44.server.memorial.domain.repository.MemorialBowRepository;
import windeath44.server.memorial.domain.repository.MemorialRepository;

@Service
@RequiredArgsConstructor
public class MemorialBowService {
  private final MemorialBowRepository memorialBowRepository;
  private final MemorialRepository memorialRepository;
  private final MemorialBowMapper memorialBowMapper;

  @Transactional
  public void bow(MemorialBowRequestDto memorialBowRequestDto) {
    String userId = memorialBowRequestDto.userId();
    Long memorialId = memorialBowRequestDto.memorialId();
    validateMemorial(memorialId);
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
    validateMemorial(memorialId);
    Long bowCount = memorialBowRepository.sumBowCount(memorialId);
    return bowCount == null ? 0 : bowCount;
  }

  public MemorialBowResponseDto findMemorialBowByUserIdAndMemorialId(String userId, Long memorialId) {
    validateMemorial(memorialId);
    MemorialBow memorialBow = memorialBowRepository.findMemorialBowByUserIdAndMemorialId(userId, memorialId);
    return memorialBowMapper.toMemorialBowResponseDto(memorialBow);
  }

  private void validateMemorial(Long memorialId) {
    memorialRepository.findById(memorialId).orElseThrow(MemorialNotFoundException::new);
  }
}
