package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialChiefs;
import windeath44.server.memorial.domain.memorial.repository.MemorialBowRepository;
import windeath44.server.memorial.domain.memorial.repository.MemorialChiefsRepository;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemorialChiefService {
  private final MemorialBowRepository memorialBowRepository;
  private final MemorialRepository memorialRepository;
  private final MemorialChiefsRepository memorialChiefsRepository;

  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")  // 매일 0시(자정)
  @Transactional
  public void updateChiefs() {
    List<Memorial> memorials = memorialRepository.findAll();
    for(Memorial memorial : memorials) {
      List<String> top3UserIds = memorialBowRepository.top3UserIds(memorial.getMemorialId());
      memorialChiefsRepository.deleteAllByMemorial_MemorialId(memorial.getMemorialId());
      List<MemorialChiefs> chiefs = top3UserIds.stream()
              .map(userId -> new MemorialChiefs(memorial, userId))
              .toList();

      memorialChiefsRepository.saveAll(chiefs);
    }
  }

  public List<String> getChiefs(Long memorialId) {
    return memorialBowRepository.top3UserIds(memorialId);
  }

  public List<Long> findMyMemorialIds(String userId) {
    List<MemorialChiefs> chiefsList = memorialChiefsRepository.findByUserId(userId);
    return chiefsList.stream()
            .map(chiefs -> chiefs.getMemorial().getMemorialId())
            .collect(Collectors.toList());
  }
}