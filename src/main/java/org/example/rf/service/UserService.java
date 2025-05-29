package org.example.rf.service;

import org.example.rf.dao.UserDAO;
import org.example.rf.model.User;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class UserService {

    private final EntityManager em;
    private final UserDAO userDAO;

    public UserService() {
        this.em = JPAUtil.getEntityManager(); // Tạo EntityManager 1 lần
        this.userDAO = new UserDAO(em);       // Tạo DAO 1 lần với EntityManager đó
    }

    // Tạo mới User
    public void createUser(User user) {
        userDAO.create(user);
    }

    // Tìm User theo ID
    public User getUserById(Long id) {
        return userDAO.findById(id);
    }

    // Tìm User theo Email
    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    // Cập nhật User
    public void updateUser(User user) {
        userDAO.update(user);
    }

    // Xóa User theo ID
    public void deleteUser(Long id) {
        userDAO.delete(id);
    }

    // Lấy danh sách tất cả User
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    // Hàm tìm user theo googleId
    public User getUserByGoogleId(String googleId) {
        return userDAO.findByGoogleId(googleId);
    }

    // Đóng EntityManager
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
