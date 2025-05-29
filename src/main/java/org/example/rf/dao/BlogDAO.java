package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Blog;

import java.util.List;

public class BlogDAO {

    private final EntityManager entityManager;

    public BlogDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Tạo blog mới
    public void create(Blog blog) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(blog);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Tìm blog theo id
    public Blog findById(Long id) {
        return entityManager.find(Blog.class, id);
    }

    // Cập nhật blog
    public void update(Blog blog) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(blog);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Xóa blog theo id
    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Blog blog = entityManager.find(Blog.class, id);
            if (blog != null) {
                entityManager.remove(blog);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả blog
    public List<Blog> findAll() {
        TypedQuery<Blog> query = entityManager.createQuery("SELECT b FROM Blog b", Blog.class);
        return query.getResultList();
    }
}
