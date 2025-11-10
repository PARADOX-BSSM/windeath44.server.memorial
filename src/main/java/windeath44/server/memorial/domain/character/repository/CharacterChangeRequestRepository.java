package windeath44.server.memorial.domain.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import windeath44.server.memorial.domain.character.model.CharacterChangeRequest;

import java.util.Optional;

@Repository
public interface CharacterChangeRequestRepository extends JpaRepository<CharacterChangeRequest, Long> {
    Optional<CharacterChangeRequest> findByMemorialCommitMemorialCommitId(Long memorialCommitId);
}
