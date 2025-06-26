package org.example.rf.service;

import org.example.rf.dao.PlanDAO;
import org.example.rf.dao.UserDAO;
import org.example.rf.dao.UserSubscriptionDAO;
import org.example.rf.model.Enrollment;
import org.example.rf.model.Plan;
import org.example.rf.model.User;
import org.example.rf.model.UserSubscription;
import org.example.rf.util.HashPassword;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final EntityManager em;
    private final UserDAO userDAO;
    private final PlanDAO planDAO;
    private final UserSubscriptionDAO subscriptionDAO;

    public UserService() {
        this.em = JPAUtil.getEntityManager(); // Tạo EntityManager 1 lần
        this.userDAO = new UserDAO(em);
        this.planDAO = new PlanDAO(em);
        this.subscriptionDAO = new UserSubscriptionDAO(em);// Tạo DAO 1 lần với EntityManager đó
    }

    // Tạo mới User
    public void createUser(User user) {
        userDAO.create(user);
        Plan freePlan = planDAO.findByName("Free");

        // 3. Tạo subscription mặc định
        UserSubscription subscription = UserSubscription.builder()
                .userId(user.getId()) // user đã được persist → có ID
                .planId(freePlan.getId())
                .startDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        subscriptionDAO.create(subscription);
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
    public boolean updateUser(User user) {
        userDAO.update(user);
        return true;
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

    public boolean checkPassword(Long userId, String rawPassword) {
        User user = userDAO.findById(userId);
        if (user == null) return false;

        String hashedInput = HashPassword.hashPassword(rawPassword);
        return user.getPassword().equals(hashedInput);
    }

    // Cập nhật mật khẩu mới (hash trước khi lưu)
    public void updatePassword(Long userId, String newPassword) {
        em.getTransaction().begin();

        User user = userDAO.findById(userId);
        if (user != null) {
            String hashed = HashPassword.hashPassword(newPassword);
            user.setPassword(hashed);
            userDAO.update(user);
        }

        em.getTransaction().commit();
    }

    // Đóng EntityManager
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }


}

