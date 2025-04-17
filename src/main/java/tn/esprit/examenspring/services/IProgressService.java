package tn.esprit.examenspring.services;

import java.util.List;

import tn.esprit.examenspring.entities.Progress;

public interface IProgressService {
    Progress createOrUpdateProgress(Integer userId, Integer formationId, int progressPercentage, boolean videosCompleted, Integer quizScore);
    Progress getProgress(Integer userId, Integer formationId);
    boolean isCourseCompleted(Integer userId, Integer formationId);
    void markVideoWatched(Integer userId, Integer formationId, Long videoId);
    List<Long> getWatchedVideoIds(Integer userId, Integer formationId);
}
