package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examenspring.entities.WatchedVideo;
import java.util.List;

public interface WatchedVideoRepository extends JpaRepository<WatchedVideo, Long> {
    List<WatchedVideo> findByUserIdAndFormationId(Integer userId, Integer formationId);
    boolean existsByUserIdAndFormationIdAndVideoId(Integer userId, Integer formationId, Long videoId);
}
