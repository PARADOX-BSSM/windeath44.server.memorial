package windeath44.server.memorial.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.MemorialChiefs;
import windeath44.server.memorial.domain.repository.MemorialBowRepository;
import windeath44.server.memorial.domain.repository.MemorialChiefsRepository;
import windeath44.server.memorial.domain.repository.MemorialRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChiefUpdateService {
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
}