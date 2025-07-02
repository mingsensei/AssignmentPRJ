package org.example.rf.dao;

import org.example.rf.model.Lesson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class LessonDAO extends GenericDAO<Lesson, Long> {
    public LessonDAO(EntityManager entityManager) {
        super(entityManager, Lesson.class);
    }

    public List<Lesson> findByCourseId(Long courseId) {
        TypedQuery<Lesson> query = entityManager.createQuery(
                "SELECT l FROM Lesson l WHERE l.course.id = :courseId", Lesson.class);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }

    public List<Lesson> findByChapterId(Long chapterId) {
        TypedQuery<Lesson> query = entityManager.createQuery(
                "SELECT l FROM Lesson l WHERE l.chapterId = :chapterId ORDER BY l.orderIndex ASC", Lesson.class);
        query.setParameter("chapterId", chapterId);
        return query.getResultList();
    }
}
