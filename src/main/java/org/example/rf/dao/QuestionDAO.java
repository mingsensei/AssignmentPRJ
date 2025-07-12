package org.example.rf.dao;

import org.example.rf.model.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class QuestionDAO {

    private final EntityManager entityManager;

    public QuestionDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Question question) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(question);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Question findById(Long id) {
        return entityManager.find(Question.class, id);
    }

    public void update(Question question) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(question);
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
            Question question = entityManager.find(Question.class, id);
            if (question != null) {
                entityManager.remove(question);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<Question> findAll() {
        TypedQuery<Question> query = entityManager.createQuery("SELECT q FROM Question q", Question.class);
        return query.getResultList();
    }

    public List<Question> findByChapterId(Long chapterId) {
        TypedQuery<Question> query = entityManager.createQuery(
                "SELECT q FROM Question q WHERE q.chapterId = :chapterId", Question.class);
        query.setParameter("chapterId", chapterId);
        return query.getResultList();
    }

    public List<Question> findByDifficulty(Integer difficulty) {
        TypedQuery<Question> query = entityManager.createQuery(
                "SELECT q FROM Question q WHERE q.difficulty = :difficulty", Question.class);
        query.setParameter("difficulty", difficulty);
        return query.getResultList();
    }

    public List<Question> findAllByChapterIdAndDifficulty(Long chapterId, Integer difficulty) {
        TypedQuery<Question> query = entityManager.createQuery(
                "SELECT q FROM Question q WHERE q.chapterId = :chapterId AND q.difficulty = :difficulty",
                Question.class);
        query.setParameter("chapterId", chapterId);
        query.setParameter("difficulty", difficulty);
        return query.getResultList();
    }

    public List<Integer> findAllDifficulties() {
        TypedQuery<Integer> query = entityManager.createQuery(
                "SELECT DISTINCT q.difficulty FROM Question q ORDER BY q.difficulty ASC", Integer.class);
        return query.getResultList();
    }

}
