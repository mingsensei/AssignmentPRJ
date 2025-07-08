package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.LearningProgress;

import java.util.List;
import java.util.Optional;

public class LearningProgressDAO extends GenericDAO<LearningProgress, Long> {

    public LearningProgressDAO(EntityManager entityManager) {
        super(entityManager, LearningProgress.class);
    }

    /**
     * Tìm tiến độ học của một người dùng theo bài học
     */
    public Optional<LearningProgress> findByUserAndLesson(Long userId, Long lessonId) {
        TypedQuery<LearningProgress> query = entityManager.createQuery(
                "SELECT lp FROM LearningProgress lp WHERE lp.user.id = :userId AND lp.lesson.id = :lessonId",
                LearningProgress.class
        );
        query.setParameter("userId", userId);
        query.setParameter("lessonId", lessonId);
        List<LearningProgress> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /**
     * Lấy tất cả các bài học đã hoàn thành của user
     */
    public List<LearningProgress> findCompletedLessonsByUser(Long userId) {
        TypedQuery<LearningProgress> query = entityManager.createQuery(
                "SELECT lp FROM LearningProgress lp WHERE lp.user.id = :userId AND lp.isCompleted = true",
                LearningProgress.class
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Lấy tiến độ học của user trong một course cụ thể
     */
    public List<LearningProgress> findByUserAndCourse(Long userId, Long courseId) {
        TypedQuery<LearningProgress> query = entityManager.createQuery(
                "SELECT lp FROM LearningProgress lp " +
                        "WHERE lp.user.id = :userId AND lp.lesson.courseId = :courseId",
                LearningProgress.class
        );
        query.setParameter("userId", userId);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }

    /**
     * Lấy tiến độ học của user trong một chapter cụ thể
     */
    public List<LearningProgress> findByUserAndChapter(Long userId, Long chapterId) {
        TypedQuery<LearningProgress> query = entityManager.createQuery(
                "SELECT lp FROM LearningProgress lp " +
                        "WHERE lp.user.id = :userId AND lp.lesson.chapterId = :chapterId",
                LearningProgress.class
        );
        query.setParameter("userId", userId);
        query.setParameter("chapterId", chapterId);
        return query.getResultList();
    }
}
