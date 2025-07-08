package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.example.rf.model.BlogUser;

public class BlogUserDAO extends GenericDAO<BlogUser, Long> {

    public BlogUserDAO(EntityManager entityManager) {
        super(entityManager, BlogUser.class);
    }

    public List<BlogUser> findByBlogId(Long blogId) {
        return entityManager.createQuery(
                "SELECT bu FROM BlogUser bu WHERE bu.blog.id = :blogId", BlogUser.class)
                .setParameter("blogId", blogId)
                .getResultList();
    }

    // Find BlogUser by user ID
    public List<BlogUser> findByUserId(Long userId) {
        return entityManager.createQuery(
                "SELECT bu FROM BlogUser bu WHERE bu.user.id = :userId", BlogUser.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // Find BlogUser by user IDs
    public List<BlogUser> findByBlogUser(Long userId, Long blogId) {
        return entityManager.createQuery(
                "SELECT bu FROM BlogUser bu WHERE bu.user.id = :userId AND bu.blog.id = :blogId", BlogUser.class)
                .setParameter("userId", userId)
                .setParameter("blogId", blogId)
                .getResultList();
    }

}
