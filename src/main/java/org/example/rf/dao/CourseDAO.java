package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Category;
import org.example.rf.model.Course;

import java.util.List;

public class CourseDAO {

    private final EntityManager entityManager;

    public CourseDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Tạo mới course
    public void create(Course course) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(course);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Tìm course theo id
    public Course findById(Long id) {
        return entityManager.find(Course.class, id);
    }

    // Cập nhật course
    public void update(Course course) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(course);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Xóa course theo id
    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Course course = entityManager.find(Course.class, id);
            if (course != null) {
                entityManager.remove(course);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả course
    public List<Course> findAll() {
        TypedQuery<Course> query = entityManager.createQuery("SELECT c FROM Course c", Course.class);
        return query.getResultList();
    }

    // Tìm danh sách course theo categoryId
    public List<Course> findByCategoryId(Long categoryId) {
        TypedQuery<Course> query = entityManager.createQuery(
                "SELECT c FROM Course c WHERE c.categoryId = :categoryId", Course.class);
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }

    // Lấy 4 category đầu tiên theo id tăng dần
    public List<Course> getTop4Courses() {
        TypedQuery<Course> query = entityManager.createQuery(
                "SELECT c FROM Course c ORDER BY c.id ASC", Course.class
        );
        query.setMaxResults(4);
        return query.getResultList();
    }
}
