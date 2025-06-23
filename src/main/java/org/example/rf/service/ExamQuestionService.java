package org.example.rf.service;

import jakarta.persistence.EntityTransaction;
import org.example.rf.dao.ExamQuestionDAO;
import org.example.rf.model.ExamQuestion;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Map;

public class ExamQuestionService {

    private final EntityManager em;
    private final ExamQuestionDAO examQuestionDAO;

    public ExamQuestionService() {
        this.em = JPAUtil.getEntityManager();  // Tạo EntityManager 1 lần
        this.examQuestionDAO = new ExamQuestionDAO(em);  // Tạo DAO 1 lần dùng chung EntityManager
    }

    // Tạo mới ExamQuestion
    public void createExamQuestion(ExamQuestion examQuestion) {
        examQuestionDAO.create(examQuestion);
    }

    // Tìm ExamQuestion theo ID
    public ExamQuestion getExamQuestionById(Long id) {
        return examQuestionDAO.findById(id);
    }

    // Cập nhật ExamQuestion
    public void updateExamQuestion(ExamQuestion examQuestion) {
        examQuestionDAO.update(examQuestion);
    }

    // Xóa ExamQuestion theo ID
    public void deleteExamQuestion(Long id) {
        examQuestionDAO.delete(id);
    }

    // Lấy danh sách tất cả ExamQuestion
    public List<ExamQuestion> getAllExamQuestions() {
        return examQuestionDAO.findAll();
    }

    // Đóng EntityManager khi không còn dùng
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    public void saveStudentAnswers(Long examId, Map<String, String> studentAnswers) {
        for (Map.Entry<String, String> entry : studentAnswers.entrySet()) {
            String questionIdStr = entry.getKey();
            String answer = entry.getValue();

            try {
                Long questionId = Long.parseLong(questionIdStr);

                // Tìm ExamQuestion tương ứng
                ExamQuestion examQuestion = examQuestionDAO.findByExamIdAndQuestionId(examId, questionId);
                if (examQuestion == null) {
                    // Thử tìm với AiQuestion
                    examQuestion = examQuestionDAO.findByExamIdAndAiQuestionId(examId, questionId);
                }

                if (examQuestion != null) {
                    examQuestion.setStudentAnswer(answer);
                    examQuestionDAO.update(examQuestion);
                }

            } catch (NumberFormatException e) {
                System.err.println("Invalid question ID format: " + questionIdStr);
            }
        }
    }
}
