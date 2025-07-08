package org.example.rf.service;

import jakarta.persistence.EntityManager;
import org.example.rf.dao.LearningProgressDAO;
import org.example.rf.dao.LessonDAO;
import org.example.rf.model.Chapter;
import org.example.rf.model.CompletionStatus;
import org.example.rf.model.LearningProgress;
import org.example.rf.model.Lesson;
import org.example.rf.util.JPAUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LearningProgressService {
    LessonDAO lessonDAO;

    private final EntityManager em;
    private final LearningProgressDAO learningProgressDAO;

    public LearningProgressService() {
        this.em = JPAUtil.getEntityManager();
        this.learningProgressDAO = new LearningProgressDAO(em);
        this.lessonDAO = new LessonDAO(em);
    }

    // Tạo mới tiến độ học
    public void createProgress(LearningProgress progress) {
        learningProgressDAO.create(progress);
    }

    // Cập nhật tiến độ học
    public void updateProgress(LearningProgress progress) {
        learningProgressDAO.update(progress);
    }

    // Tìm theo ID
    public LearningProgress getProgressById(Long id) {
        return learningProgressDAO.findById(id);
    }

    // Xóa tiến độ học
    public void deleteProgress(Long id) {
        learningProgressDAO.delete(id);
    }

    // Tìm tất cả tiến độ học
    public List<LearningProgress> getAllProgress() {
        return learningProgressDAO.findAll();
    }

    // Tìm tiến độ học của user theo bài học
    public Optional<LearningProgress> getProgressByUserAndLesson(Long userId, Long lessonId) {
        return learningProgressDAO.findByUserAndLesson(userId, lessonId);
    }

    // Lấy tất cả bài học user đã học xong
    public List<LearningProgress> getCompletedLessonsByUser(Long userId) {
        return learningProgressDAO.findCompletedLessonsByUser(userId);
    }

    // Lấy toàn bộ tiến độ học trong 1 khóa học
    public List<LearningProgress> getProgressByUserAndCourse(Long userId, Long courseId) {
        return learningProgressDAO.findByUserAndCourse(userId, courseId);
    }

    // Lấy toàn bộ tiến độ học trong 1 chương
    public List<LearningProgress> getProgressByUserAndChapter(Long userId, Long chapterId) {
        return learningProgressDAO.findByUserAndChapter(userId, chapterId);
    }

    // Đóng entity manager
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }


    public CompletionStatus getCompletedStatusByCourse(Long userId, Long courseId) {
        // 1. Lấy tất cả bài học đã hoàn thành của user
        List<LearningProgress> completedProgress = learningProgressDAO.findCompletedLessonsByUser(userId);
        Set<Long> completedLessonIds = completedProgress.stream()
                .filter(LearningProgress::getIsCompleted)
                .map(lp -> lp.getLesson().getId())
                .collect(Collectors.toSet());

        // 2. Lấy toàn bộ chapter của khóa học
        ChapterService chapterService = new ChapterService();
        LessonService lessonService = new LessonService();

        List<Chapter> courseChapters = chapterService.getChaptersByCourseId(courseId);

        Set<Long> completedChapterIds = new HashSet<>();
        int totalLessons = 0;
        int totalCompletedLessons = 0;

        for (Chapter chapter : courseChapters) {
            List<Lesson> lessons = lessonService.getLessonsByChapterId(chapter.getId());
            totalLessons += lessons.size();

            boolean allLessonsCompleted = true;
            for (Lesson lesson : lessons) {
                if (!completedLessonIds.contains(lesson.getId())) {
                    allLessonsCompleted = false;
                } else {
                    totalCompletedLessons++;
                }
            }

            if (allLessonsCompleted && !lessons.isEmpty()) {
                completedChapterIds.add(chapter.getId());
            }
        }

        boolean courseCompleted = totalLessons > 0 && totalCompletedLessons == totalLessons;

        return new CompletionStatus(courseCompleted, completedChapterIds, completedLessonIds);
    }

}
