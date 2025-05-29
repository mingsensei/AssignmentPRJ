package org.example.rf.service;

import org.example.rf.dao.AiQuestionDAO;
import org.example.rf.model.AiQuestion;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class AiQuestionService {

    private final EntityManager em;
    private final AiQuestionDAO dao;

    public AiQuestionService() {
        this.em = JPAUtil.getEntityManager();   // Tạo EntityManager 1 lần
        this.dao = new AiQuestionDAO(em);       // Tạo DAO 1 lần dùng lại
    }

    // Create
    public void createAiQuestion(AiQuestion aiQuestion) {
        dao.create(aiQuestion);
    }

    // Read by ID
    public AiQuestion getAiQuestionById(Long id) {
        return dao.findById(id);
    }

    // Read all
    public List<AiQuestion> getAllAiQuestions() {
        return dao.findAll();
    }

    // Update
    public void updateAiQuestion(AiQuestion aiQuestion) {
        dao.update(aiQuestion);
    }

    // Delete
    public void deleteAiQuestion(Long id) {
        dao.delete(id);
    }

    // Cleanup
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
