package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Enrollment;

import java.util.List;

public class EnrollmentDAO {

    private final EntityManager entityManager;

    public EnrollmentDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Tạo mới Enrollment
    public void create(Enrollment enrollment) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(enrollment);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Tìm Enrollment theo id
    public Enrollment findById(Long id) {
        return entityManager.find(Enrollment.class, id);
    }

    // Cập nhật Enrollment
    public void update(Enrollment enrollment) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(enrollment);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Xóa Enrollment theo id
    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Enrollment enrollment = entityManager.find(Enrollment.class, id);
            if (enrollment != null) {
                entityManager.remove(enrollment);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả Enrollment
    public List<Enrollment> findAll() {
        TypedQuery<Enrollment> query = entityManager.createQuery("SELECT e FROM Enrollment e", Enrollment.class);
        return query.getResultList();
    }
    // Lấy danh sách Enrollment theo userId
    public List<Enrollment> findByUserId(Long userId) {
        TypedQuery<Enrollment> query = entityManager.createQuery(
                "SELECT e FROM Enrollment e WHERE e.user.id = :userId", Enrollment.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

}
