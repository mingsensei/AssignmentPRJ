package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;
import org.example.rf.model.Category;

import java.util.List;

public class CategoryDAO extends GenericDAO<Category, Long> {

    public CategoryDAO(EntityManager entityManager) {
        super(entityManager, Category.class);
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

    // Lấy 4 category đầu tiên theo id tăng dần
    public List<Category> getTop4Categories() {
        TypedQuery<Category> query = entityManager.createQuery(
                "SELECT c FROM Category c ORDER BY c.id ASC", Category.class
        );
        query.setMaxResults(4);
        return query.getResultList();
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

    public List<Category> findAll() {
        TypedQuery<Category> query = entityManager.createQuery("SELECT c FROM Category c", Category.class);
        return query.getResultList();
    }

    public boolean existsByName(String name, Long excludeId) {
        String jpql = "SELECT COUNT(c) FROM Category c WHERE LOWER(c.name) = LOWER(:name)";

        if (excludeId != null) {
            jpql += " AND c.id != :excludeId";
        }

        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("name", name);

        if (excludeId != null) {
            query.setParameter("excludeId", excludeId);
        }

        return query.getSingleResult() > 0;
    }

    public boolean isCategoryInUse(Long categoryId) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(c) FROM Course c WHERE c.categoryId = :categoryId", Long.class
            );
            query.setParameter("categoryId", categoryId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public Category findByName(String name) {
        try {
            TypedQuery<Category> query = entityManager.createQuery(
                    "SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name)", Category.class
            );
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public long countAll() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(c) FROM Category c", Long.class);
        return query.getSingleResult();
    }

    public List<Category> searchByKeyword(String keyword) {
        TypedQuery<Category> query = entityManager.createQuery(
                "SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(:keyword) OR LOWER(c.description) LIKE LOWER(:keyword)",
                Category.class
        );
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
    }
}