package org.example.rf.service;

import org.example.rf.dao.LevelDAO;
import org.example.rf.model.Level;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class LevelService {

    private final EntityManager em;
    private final LevelDAO levelDAO;

    public LevelService() {
        this.em = JPAUtil.getEntityManager(); // Tạo EntityManager 1 lần
        this.levelDAO = new LevelDAO(em);     // Tạo DAO 1 lần dùng chung EntityManager
    }

    // Tạo mới Level
    public void createLevel(Level level) {
        levelDAO.create(level);
    }

    // Tìm Level theo ID
    public Level getLevelById(Long id) {
        return levelDAO.findById(id);
    }

    // Cập nhật Level
    public void updateLevel(Level level) {
        levelDAO.update(level);
    }

    // Xóa Level theo ID
    public void deleteLevel(Long id) {
        levelDAO.delete(id);
    }

    // Lấy danh sách tất cả Level
    public List<Level> getAllLevels() {
        return levelDAO.findAll();
    }

    // Đóng EntityManager khi không còn dùng
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
