package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.AiQuestion;
import org.example.rf.model.Question;

import java.util.List;

public class AiQuestionDAO {

    private final EntityManager entityManager;

    public AiQuestionDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Thêm mới AI Question
    public void create(AiQuestion aiQuestion) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(aiQuestion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    // Tìm AI Question theo id
    public AiQuestion findById(Long id) {
        return entityManager.find(AiQuestion.class, id);
    }

    // Cập nhật AI Question
    public void update(AiQuestion aiQuestion) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(aiQuestion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    // Xóa AI Question theo id
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            AiQuestion aiQuestion = entityManager.find(AiQuestion.class, id);
            if (aiQuestion != null) {
                entityManager.remove(aiQuestion);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả AI Question
    public List<AiQuestion> findAll() {
        TypedQuery<AiQuestion> query = entityManager.createQuery("SELECT a FROM AiQuestion a", AiQuestion.class);
        return query.getResultList();
    }

    public List<AiQuestion> findByDifficulty(int difficulty) {
        TypedQuery<AiQuestion> query = entityManager.createQuery(
                "SELECT q FROM AiQuestion q WHERE q.difficulty = :difficulty", AiQuestion.class);
        query.setParameter("difficulty", difficulty);
        return query.getResultList();
    }
}
