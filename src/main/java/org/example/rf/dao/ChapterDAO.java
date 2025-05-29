package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Chapter;

import java.util.List;

public class ChapterDAO {

    private final EntityManager entityManager;

    public ChapterDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Tạo mới Chapter
    public void create(Chapter chapter) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(chapter);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Tìm Chapter theo id
    public Chapter findById(Long id) {
        return entityManager.find(Chapter.class, id);
    }

    // Cập nhật Chapter
    public void update(Chapter chapter) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(chapter);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Xóa Chapter theo id
    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Chapter chapter = entityManager.find(Chapter.class, id);
            if (chapter != null) {
                entityManager.remove(chapter);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả Chapter
    public List<Chapter> findAll() {
        TypedQuery<Chapter> query = entityManager.createQuery("SELECT c FROM Chapter c", Chapter.class);
        return query.getResultList();
    }

    // Tìm danh sách Chapter theo courseId
    public List<Chapter> findByCourseId(Long courseId) {
        TypedQuery<Chapter> query = entityManager.createQuery(
                "SELECT c FROM Chapter c WHERE c.courseId = :courseId ORDER BY c.orderIndex ASC", Chapter.class);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }
}
