package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.ExamQuestion;

import java.util.List;

public class ExamQuestionDAO {

    private final EntityManager entityManager;

    public ExamQuestionDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(ExamQuestion examQuestion) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(examQuestion);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public ExamQuestion findById(Long id) {
        return entityManager.find(ExamQuestion.class, id);
    }

    public void update(ExamQuestion examQuestion) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(examQuestion);
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
            ExamQuestion examQuestion = entityManager.find(ExamQuestion.class, id);
            if (examQuestion != null) {
                entityManager.remove(examQuestion);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<ExamQuestion> findAll() {
        TypedQuery<ExamQuestion> query = entityManager.createQuery("SELECT eq FROM ExamQuestion eq", ExamQuestion.class);
        return query.getResultList();
    }

    public List<ExamQuestion> findByExamId(Long examId) {
        TypedQuery<ExamQuestion> query = entityManager.createQuery(
                "SELECT eq FROM ExamQuestion eq WHERE eq.exam.id = :examId", ExamQuestion.class);
        query.setParameter("examId", examId);
        return query.getResultList();
    }

    public ExamQuestion findByExamIdAndQuestionId(Long examId, Long questionId) {
        TypedQuery<ExamQuestion> query = entityManager.createQuery(
                "SELECT eq FROM ExamQuestion eq WHERE eq.examId = :examId AND eq.questionId = :questionId",
                ExamQuestion.class
        );
        query.setParameter("examId", examId);
        query.setParameter("questionId", questionId);

        // Dùng try-catch để tránh lỗi nếu không tìm thấy
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ExamQuestion findByExamIdAndAiQuestionId(Long examId, Long aiQuestionId) {
        TypedQuery<ExamQuestion> query = entityManager.createQuery(
                "SELECT eq FROM ExamQuestion eq WHERE eq.examId = :examId AND eq.aiQuestionId = :aiQuestionId",
                ExamQuestion.class
        );
        query.setParameter("examId", examId);
        query.setParameter("aiQuestionId", aiQuestionId);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ExamQuestion> findAnsweredExamQuestionByExamId(Long examId) {
        String jpql = """
        SELECT eq
        FROM ExamQuestion eq
        WHERE eq.examId = :examId
          AND eq.studentAnswer IS NOT NULL
    """;

        return entityManager.createQuery(jpql, ExamQuestion.class)
                .setParameter("examId", examId)
                .getResultList();
    }

}
