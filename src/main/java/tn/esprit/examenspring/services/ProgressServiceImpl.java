package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.*;
import tn.esprit.examenspring.entities.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProgressServiceImpl implements IProgressService {
    @Autowired
    private ProgressRepository progressRepository;
    @Autowired
    private UserRepository studentRepository;
    @Autowired
    private FormationRepository formationRepository;
    @Autowired
    private RessourceRepository ressourceRepository;
    @Autowired
    private QuizScoreRepository quizScoreRepository;
    @Autowired
    private WatchedVideoRepository watchedVideoRepository;

    private static final double VIDEO_WEIGHT = 0.7; // Videos contribute 70% to progress
    private static final double QUIZ_WEIGHT = 0.3;  // Quiz contributes 30% to progress
    private static final int MAX_QUIZ_SCORE = 100;  // Assume quiz scores are out of 100
    private static final int PASSING_QUIZ_SCORE = 70; // Quiz score threshold for certificate

    @Override
    public Progress createOrUpdateProgress(Integer userId, Integer formationId, int progressPercentage, boolean videosCompleted, Integer quizScore) {
        log.info("Creating/Updating progress for userId: {}, formationId: {}", userId, formationId);

        User student = studentRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));

        Progress progress = progressRepository.findByUserIdAndFormationId(userId, formationId)
                .orElse(new Progress());
        progress.setUser(student);
        progress.setFormation(formation);

        // Update videosCompleted and quizScore
        progress.setVideosCompleted(videosCompleted);
        progress.setQuizScore(quizScore);

        // Calculate progress percentage based on certificate eligibility
        int calculatedProgress = calculateProgressPercentage(userId.longValue(), formationId, videosCompleted, quizScore);
        progress.setProgressPercentage(calculatedProgress);

        progress.setLastUpdated(new Date());
        return progressRepository.save(progress);
    }

    @Override
    public Progress getProgress(Integer userId, Integer formationId) {
        log.info("Fetching progress for userId: {}, formationId: {}", userId, formationId);
        Progress progress = progressRepository.findByUserIdAndFormationId(userId, formationId)
                .orElseGet(() -> {
                    log.info("No progress found for userId: {}, formationId: {}. Creating new progress record.", userId, formationId);
                    return createOrUpdateProgress(userId, formationId, 0, false, null);
                });

        // Recalculate progress percentage for display
        int calculatedProgress = calculateProgressPercentage(userId.longValue(), formationId, progress.isVideosCompleted(), progress.getQuizScore());
        progress.setProgressPercentage(calculatedProgress);
        progress.setLastUpdated(new Date());
        return progressRepository.save(progress);
    }

    @Override
    public boolean isCourseCompleted(Integer userId, Integer formationId) {
        log.info("Checking if course is completed for userId: {}, formationId: {}", userId, formationId);
        Progress progress = getProgress(userId, formationId);
        boolean eligibleForCertificate = progress.isVideosCompleted() &&
                progress.getQuizScore() != null &&
                progress.getQuizScore() > PASSING_QUIZ_SCORE;
        log.info("Certificate eligibility for userId: {}, formationId: {}: {}", userId, formationId, eligibleForCertificate);
        return eligibleForCertificate;
    }

    @Override
    public void markVideoWatched(Integer userId, Integer formationId, Long videoId) {
        if (!watchedVideoRepository.existsByUserIdAndFormationIdAndVideoId(userId, formationId, videoId)) {
            WatchedVideo watched = new WatchedVideo();
            watched.setUser(studentRepository.findById(userId).orElseThrow());
            watched.setFormation(formationRepository.findById(formationId).orElseThrow());
            watched.setVideoId(videoId);
            watchedVideoRepository.save(watched);
        }
        // Always recalculate progress based on videos watched (partial progress support)
        List<Ressource> allVideos = ressourceRepository.findByFormationId(formationId);
        List<Long> watchedIds = getWatchedVideoIds(userId, formationId);
        boolean allVideosWatched = !allVideos.isEmpty() && allVideos.stream().allMatch(v -> watchedIds.contains(v.getId()));
        double videoCompletion = allVideos.isEmpty() ? 0 : ((double) watchedIds.size() / allVideos.size());
        Progress progress = getProgress(userId, formationId);
        int quizScore = progress.getQuizScore() != null ? progress.getQuizScore() : 0;
        // Progress = 70% video + 30% quiz
        int progressPercentage = (int) Math.round(videoCompletion * 100 * VIDEO_WEIGHT + (quizScore / 100.0) * 100 * QUIZ_WEIGHT);
        progress.setProgressPercentage(Math.min(progressPercentage, 99));
        progress.setVideosCompleted(allVideosWatched);
        progressRepository.save(progress);
    }

    @Override
    public List<Long> getWatchedVideoIds(Integer userId, Integer formationId) {
        return watchedVideoRepository.findByUserIdAndFormationId(userId, formationId)
            .stream().map(WatchedVideo::getVideoId).collect(Collectors.toList());
    }

    private int calculateProgressPercentage(Long userId, Integer formationId, boolean videosCompleted, Integer quizScore) {
        log.debug("Calculating progress percentage for userId: {}, formationId: {}", userId, formationId);

        // Check if the user is eligible for a certificate
        boolean isEligibleForCertificate = videosCompleted &&
                (quizScore != null && quizScore > PASSING_QUIZ_SCORE);

        if (isEligibleForCertificate) {
            log.debug("User is eligible for certificate. Setting progress to 100%.");
            return 100;
        }

        // If not eligible, calculate progress based on videos and quiz
        // Video progress: 70% weight (100% if videosCompleted is true, 0% otherwise)
        double videoProgress = videosCompleted ? 100 * VIDEO_WEIGHT : 0;
        log.info("Video progress for userId: {}, formationId: {}: {}%", userId, formationId, videoProgress);

        // Quiz progress: 30% weight (proportional to quiz score out of 100)
        double quizProgress = 0;
        if (quizScore != null) {
            quizProgress = (quizScore * 1.0 / MAX_QUIZ_SCORE) * 100 * QUIZ_WEIGHT;
            log.info("Quiz progress for userId: {}, formationId: {}: {}%", userId, formationId, quizProgress);
        } else {
            log.warn("Quiz score is null for userId: {}, formationId: {}", userId, formationId);
        }

        // Total progress
        int totalProgress = (int) Math.round(videoProgress + quizProgress);
        log.info("Calculated total progress for userId: {}, formationId: {}: {}%", userId, formationId, totalProgress);

        // Cap at 99% if not eligible for certificate
        return Math.min(totalProgress, 99);
    }
}