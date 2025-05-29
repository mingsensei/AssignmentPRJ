package org.example.rf.dao;

import org.example.rf.model.Level;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class LevelDAO {

    private final EntityManager entityManager;

    public LevelDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Level level) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(level);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Level findById(Long id) {
        return entityManager.find(Level.class, id);
    }

    public void update(Level level) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(level);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Level level = entityManager.find(Level.class, id);
            if (level != null) {
                entityManager.remove(level);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<Level> findAll() {
        TypedQuery<Level> query = entityManager.createQuery("SELECT l FROM Level l", Level.class);
        return query.getResultList();
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
