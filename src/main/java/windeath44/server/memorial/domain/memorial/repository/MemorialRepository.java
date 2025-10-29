package windeath44.server.memorial.domain.memorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialResponseDto;
import windeath44.server.memorial.domain.memorial.model.Memorial;

import java.util.List;

@Repository
public interface MemorialRepository extends JpaRepository<Memorial, Long>, MemorialRepositoryCustom {
  Memorial findMemorialByCharacterId(Long characterId);
  List<Memorial> findByCreatorId(String creatorId);
}
