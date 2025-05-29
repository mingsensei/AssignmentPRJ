package org.example.rf.service;

import org.example.rf.dao.ExamDAO;
import org.example.rf.model.Exam;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class ExamService {

    private final EntityManager em;
    private final ExamDAO examDAO;

    public ExamService() {
        this.em = JPAUtil.getEntityManager();  // Tạo EntityManager 1 lần
        this.examDAO = new ExamDAO(em);        // Tạo DAO 1 lần dùng chung EntityManager
    }

    // Tạo mới Exam
    public void createExam(Exam exam) {
        examDAO.create(exam);
    }

    // Tìm Exam theo ID
    public Exam getExamById(Long id) {
        return examDAO.findById(id);
    }

    // Cập nhật Exam
    public void updateExam(Exam exam) {
        examDAO.update(exam);
    }

    // Xóa Exam theo ID
    public void deleteExam(Long id) {
        examDAO.delete(id);
    }

    // Lấy danh sách tất cả Exam
    public List<Exam> getAllExams() {
        return examDAO.findAll();
    }

    // Đóng EntityManager khi không còn dùng
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
