package org.example.rf.service;

import org.example.rf.dao.QuestionDAO;
import org.example.rf.model.Question;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class QuestionService {

    private final EntityManager em;
    private final QuestionDAO questionDAO;

    public QuestionService() {
        this.em = JPAUtil.getEntityManager(); // tạo EntityManager 1 lần
        this.questionDAO = new QuestionDAO(em); // tạo DAO 1 lần với EntityManager đó
    }

    // Tạo mới câu hỏi
    public void createQuestion(Question question) {
        questionDAO.create(question);
    }

    // Tìm câu hỏi theo ID
    public Question getQuestionById(Long id) {
        return questionDAO.findById(id);
    }

    // Cập nhật câu hỏi
    public void updateQuestion(Question question) {
        questionDAO.update(question);
    }

    // Xóa câu hỏi theo ID
    public void deleteQuestion(Long id) {
        questionDAO.delete(id);
    }

    // Lấy danh sách tất cả câu hỏi
    public List<Question> getAllQuestions() {
        return questionDAO.findAll();
    }

    // Đóng EntityManager khi không dùng nữa
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }


}
