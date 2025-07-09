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

    // Tìm kiếm nâng cao theo nhiều từ khóa (không phân biệt hoa thường)
    public List<Course> searchCourses(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        String[] tokens = keyword.toLowerCase().trim().split("\\s+");
        StringBuilder jpql = new StringBuilder("SELECT c FROM Course c WHERE ");
        for (int i = 0; i < tokens.length; i++) {
            if (i > 0) jpql.append(" OR ");
            jpql.append("(LOWER(c.name) LIKE :kw").append(i)
                .append(" OR LOWER(c.description) LIKE :kw").append(i).append(')');
        }

        TypedQuery<Course> query = entityManager.createQuery(jpql.toString(), Course.class);
        for (int i = 0; i < tokens.length; i++) {
            query.setParameter("kw" + i, "%" + tokens[i] + "%");
        }
        return query.getResultList();
    }
}
