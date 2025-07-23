package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Enrollment;
import org.example.rf.util.JPAUtil; // Giả định bạn có class tiện ích này

import java.util.List;

public class EnrollmentDAO {

    // Constructor rỗng, không phụ thuộc vào EntityManager từ bên ngoài
    public EnrollmentDAO() {
    }

    // Tạo mới Enrollment
    public void create(Enrollment enrollment) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(enrollment);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Tìm Enrollment theo id
    public Enrollment findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Enrollment.class, id);
        } finally {
            em.close();
        }
    }

    // Cập nhật Enrollment
    public void update(Enrollment enrollment) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(enrollment);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Xóa Enrollment theo id
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Enrollment enrollment = em.find(Enrollment.class, id);
            if (enrollment != null) {
                em.remove(enrollment);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Lấy danh sách tất cả Enrollment
    public List<Enrollment> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Enrollment> query = em.createQuery("SELECT e FROM Enrollment e", Enrollment.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Lấy danh sách Enrollment theo userId
    public List<Enrollment> findByUserId(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Enrollment> query = em.createQuery(
                    "SELECT e FROM Enrollment e WHERE e.user.id = :userId", Enrollment.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}