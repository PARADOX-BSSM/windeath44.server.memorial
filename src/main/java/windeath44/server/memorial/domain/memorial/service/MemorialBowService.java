package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialBowRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialBowResponseDto;
import windeath44.server.memorial.domain.memorial.exception.BowedWithin24HoursException;
import windeath44.server.memorial.domain.memorial.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.memorial.mapper.MemorialBowMapper;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialBow;
import windeath44.server.memorial.domain.memorial.repository.MemorialBowRepository;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemorialBowService {
  private final MemorialBowRepository memorialBowRepository;
  private final MemorialRepository memorialRepository;
  private final MemorialBowMapper memorialBowMapper;

  @Transactional
  public void bow(String userId, MemorialBowRequestDto memorialBowRequestDto) {
    Long memorialId = memorialBowRequestDto.memorialId();
    Memorial memorial = memorialRepository.findById(memorialId).orElseThrow(MemorialNotFoundException::new);
    MemorialBow memorialBow = memorialBowRepository.findMemorialBowByUserIdAndMemorialId(userId, memorialId);
    if(memorialBow == null) {
      MemorialBow newMemorialBow = new MemorialBow(
              userId,
              memorialId
      );
      memorialBowRepository.save(newMemorialBow);
    }
    else {
      if(memorialBow.getLastBowedAt().isAfter(LocalDateTime.now().minusDays(1))) {
        throw new BowedWithin24HoursException();
      }
      memorialBow.plusBowCount();
      memorialBowRepository.save(memorialBow);
    }
    memorial.plusBowCount();
    memorialRepository.save(memorial);
  }

  public Long bowCountByMemorialId(Long memorialId) {
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
