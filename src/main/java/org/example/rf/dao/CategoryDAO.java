package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Category;

import java.util.List;

public class CategoryDAO {

    private final EntityManager entityManager;

    public CategoryDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Tạo mới category
    public void create(Category category) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(category);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Tìm category theo id
    public Category findById(Long id) {
        return entityManager.find(Category.class, id);
    }

    // Cập nhật category
    public void update(Category category) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(category);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Xóa category theo id
    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Category category = entityManager.find(Category.class, id);
            if (category != null) {
                entityManager.remove(category);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả categories
    public List<Category> findAll() {
        TypedQuery<Category> query = entityManager.createQuery("SELECT c FROM Category c", Category.class);
        return query.getResultList();
    }
}
