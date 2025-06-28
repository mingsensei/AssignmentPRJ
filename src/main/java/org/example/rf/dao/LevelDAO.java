package org.example.rf.dao;

import org.example.rf.model.Level;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class LevelDAO extends GenericDAO<Level, Long> {
    public LevelDAO(EntityManager entityManager) {
        super(entityManager, Level.class);
    }

    public List<Level> findByStudentId(Long studentId) {
        TypedQuery<Level> query = entityManager.createQuery(
                "SELECT l FROM Level l WHERE l.studentId = :studentId", Level.class);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }

    public List<Level> findByChapterId(Long chapterId) {
        TypedQuery<Level> query = entityManager.createQuery(
                "SELECT l FROM Level l WHERE l.chapterId = :chapterId", Level.class);
        query.setParameter("chapterId", chapterId);
        return query.getResultList();
    }

    public Level findByStudentIdAndChapterId(Long studentId, Long chapterId) {
        TypedQuery<Level> query = entityManager.createQuery(
                "SELECT l FROM Level l WHERE l.studentId = :studentId AND l.chapterId = :chapterId", Level.class);
        query.setParameter("studentId", studentId);
        query.setParameter("chapterId", chapterId);
        return query.getResultStream().findFirst().orElse(null);
    }
}
