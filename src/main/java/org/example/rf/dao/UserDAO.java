package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.User;

import java.util.List;

public class UserDAO {

    private final EntityManager entityManager;

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Thêm mới User
    public void create(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    // Tìm User theo id
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    // Cập nhật User
    public void update(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    // Xóa User theo id
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả User
    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    // Tìm User theo email (ví dụ thêm phương thức tìm theo thuộc tính riêng)
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst().orElse(null);
    }

    // Tìm User theo GoogleId (ví dụ thêm phương thức tìm theo googleId)
    public User findByGoogleId(String googleId) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.googleId = :googleId", User.class);
        query.setParameter("googleId", googleId);
        return query.getResultStream().findFirst().orElse(null);
    }
}
