package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.avro.MemorialBowedAvroSchema;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialBowRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialBowResponseDto;
import windeath44.server.memorial.domain.memorial.exception.BowedWithin24HoursException;
import windeath44.server.memorial.domain.memorial.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.memorial.mapper.MemorialBowMapper;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialBow;
import windeath44.server.memorial.domain.memorial.repository.MemorialBowRepository;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;
import windeath44.server.memorial.global.infrastructure.KafkaProducer;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemorialBowService {
  private final MemorialBowRepository memorialBowRepository;
  private final MemorialRepository memorialRepository;
  private final MemorialBowMapper memorialBowMapper;
  private final KafkaProducer kafkaProducer;

  @Transactional
  public void bow(String userId, MemorialBowRequestDto memorialBowRequestDto) throws BowedWithin24HoursException {
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
        Duration remaining = Duration.between(LocalDateTime.now(), memorialBow.getLastBowedAt().plusDays(1));
        long totalSeconds = Math.max(0, remaining.getSeconds());
        long hours = totalSeconds / 3600; long minutes = (totalSeconds % 3600) / 60; long seconds = totalSeconds % 60;
        String formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        throw new BowedWithin24HoursException(formatted);
      }
      memorialBow.plusBowCount();
      memorialBowRepository.save(memorialBow);
    }
    memorial.plusBowCount();
    memorialRepository.save(memorial);

    // 절 이벤트
    MemorialBowedAvroSchema memorialBowedAvroSchema = new MemorialBowedAvroSchema(memorialId, memorial.getBowCount(), userId);
    kafkaProducer.send("memorial-bowed-request", memorialBowedAvroSchema);
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
