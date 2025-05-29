package org.example.rf.dao;

import org.example.rf.model.Lesson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class LessonDAO {

    private final EntityManager entityManager;

    public LessonDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Lesson lesson) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(lesson);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Lesson findById(Long id) {
        return entityManager.find(Lesson.class, id);
    }

    public void update(Lesson lesson) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(lesson);
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
            Lesson lesson = entityManager.find(Lesson.class, id);
            if (lesson != null) {
                entityManager.remove(lesson);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<Lesson> findAll() {
        TypedQuery<Lesson> query = entityManager.createQuery("SELECT l FROM Lesson l", Lesson.class);
        return query.getResultList();
    }

    public List<Lesson> findByCourseId(Long courseId) {
        TypedQuery<Lesson> query = entityManager.createQuery(
                "SELECT l FROM Lesson l WHERE l.course.id = :courseId", Lesson.class);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }

    public List<Lesson> findByChapterId(Long chapterId) {
        TypedQuery<Lesson> query = entityManager.createQuery(
                "SELECT l FROM Lesson l WHERE l.chapter.id = :chapterId", Lesson.class);
        query.setParameter("chapterId", chapterId);
        return query.getResultList();
    }
}
