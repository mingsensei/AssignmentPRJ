package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Chapter;
import java.util.List;

public class ChapterDAO extends GenericDAO<Chapter, Long> {
    public ChapterDAO(EntityManager entityManager) {
        super(entityManager, Chapter.class);
    }

    public List<Chapter> findByCourseId(Long courseId) {
        TypedQuery<Chapter> query = entityManager.createQuery(
                "SELECT c FROM Chapter c WHERE c.courseId = :courseId ORDER BY c.orderIndex ASC", Chapter.class);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }
}
