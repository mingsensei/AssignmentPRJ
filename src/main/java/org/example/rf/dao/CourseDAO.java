package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Course;
import java.util.List;

public class CourseDAO extends GenericDAO<Course, Long> {
    public CourseDAO(EntityManager entityManager) {
        super(entityManager, Course.class);
    }

    // Tìm danh sách course theo categoryId
    public List<Course> findByCategoryId(Long categoryId) {
        TypedQuery<Course> query = entityManager.createQuery(
                "SELECT c FROM Course c WHERE c.categoryId = :categoryId", Course.class);
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }

    // Lấy 4 course đầu tiên theo id tăng dần
    public List<Course> getTop4Courses() {
        TypedQuery<Course> query = entityManager.createQuery(
                "SELECT c FROM Course c ORDER BY c.id ASC", Course.class
        );
        query.setMaxResults(4);
        return query.getResultList();
    }
}
