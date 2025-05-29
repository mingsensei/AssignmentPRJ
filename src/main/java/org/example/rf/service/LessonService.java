package org.example.rf.service;

import org.example.rf.dao.LessonDAO;
import org.example.rf.model.Lesson;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class LessonService {

    private final EntityManager em;
    private final LessonDAO lessonDAO;

    public LessonService() {
        this.em = JPAUtil.getEntityManager(); // Tạo EntityManager 1 lần
        this.lessonDAO = new LessonDAO(em);   // Tạo DAO 1 lần dùng chung EntityManager
    }

    // Tạo mới Lesson
    public void createLesson(Lesson lesson) {
        lessonDAO.create(lesson);
    }

    // Tìm Lesson theo ID
    public Lesson getLessonById(Long id) {
        return lessonDAO.findById(id);
    }

    // Cập nhật Lesson
    public void updateLesson(Lesson lesson) {
        lessonDAO.update(lesson);
    }

    // Xóa Lesson theo ID
    public void deleteLesson(Long id) {
        lessonDAO.delete(id);
    }

    // Lấy danh sách tất cả Lesson
    public List<Lesson> getAllLessons() {
        return lessonDAO.findAll();
    }

    // Đóng EntityManager khi không còn dùng
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
