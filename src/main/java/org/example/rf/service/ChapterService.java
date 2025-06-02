package org.example.rf.service;

import org.example.rf.dao.ChapterDAO;
import org.example.rf.model.Chapter;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class ChapterService {

    private final EntityManager em;
    private final ChapterDAO chapterDAO;

    public ChapterService() {
        this.em = JPAUtil.getEntityManager(); // Tạo EntityManager 1 lần
        this.chapterDAO = new ChapterDAO(em); // Tạo DAO 1 lần dùng chung
    }
    public List<Chapter> getChaptersByCourseId(Long courseId) {
        return chapterDAO.findByCourseId(courseId);
    }
    // Tạo chương mới
    public void createChapter(Chapter chapter) {
        chapterDAO.create(chapter);
    }

    // Tìm chương theo ID
    public Chapter getChapterById(Long id) {
        return chapterDAO.findById(id);
    }

    // Cập nhật chương
    public void updateChapter(Chapter chapter) {
        chapterDAO.update(chapter);
    }

    // Xóa chương theo ID
    public void deleteChapter(Long id) {
        chapterDAO.delete(id);
    }

    // Lấy danh sách tất cả chương
    public List<Chapter> getAllChapters() {
        return chapterDAO.findAll();
    }

    // Đóng EntityManager khi không cần nữa
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
