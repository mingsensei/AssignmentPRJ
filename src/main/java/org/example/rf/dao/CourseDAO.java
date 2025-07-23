package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Course;
import org.example.rf.util.JPAUtil; // Đảm bảo bạn có class này

import java.util.List;

public class CourseDAO {

    public CourseDAO() {
        // Constructor rỗng
    }

    // Các phương thức CRUD cơ bản, tự quản lý EntityManager

    public void create(Course course) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(course);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e; // Ném lại exception để tầng service có thể xử lý
        } finally {
            em.close();
        }
    }

    public Course findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Course.class, id);
        } finally {
            em.close();
        }
    }

    public List<Course> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c", Course.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(Course course) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(course);
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

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Course course = em.find(Course.class, id);
            if (course != null) {
                em.remove(course);
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

    // Các phương thức tùy chỉnh của bạn

    public List<Course> findByCategoryId(Long categoryId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT c FROM Course c WHERE c.categoryId = :categoryId", Course.class);
            query.setParameter("categoryId", categoryId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Course> getTop4Courses() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT c FROM Course c ORDER BY c.id ASC", Course.class
            );
            query.setMaxResults(4);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}