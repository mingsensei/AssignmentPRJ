package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.BlogUser;

import java.util.List;

public class BlogUserDAO {

    private final EntityManager entityManager;

    public BlogUserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Tạo mới BlogUser
    public void create(BlogUser blogUser) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(blogUser);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Tìm BlogUser theo id
    public BlogUser findById(Long id) {
        return entityManager.find(BlogUser.class, id);
    }

    // Cập nhật BlogUser
    public void update(BlogUser blogUser) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(blogUser);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Xóa BlogUser theo id
    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            BlogUser blogUser = entityManager.find(BlogUser.class, id);
            if (blogUser != null) {
                entityManager.remove(blogUser);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả BlogUser
    public List<BlogUser> findAll() {
        TypedQuery<BlogUser> query = entityManager.createQuery("SELECT bu FROM BlogUser bu", BlogUser.class);
        return query.getResultList();
    }
}
