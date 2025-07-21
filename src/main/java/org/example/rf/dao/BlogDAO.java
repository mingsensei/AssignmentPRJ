package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import org.example.rf.model.Blog;

public class BlogDAO extends GenericDAO<Blog, Long> {

    public BlogDAO(EntityManager entityManager) {
        super(entityManager, Blog.class);
    }

    @Transactional
    public void incrementViewCount(Long blogId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Blog blog = entityManager.find(Blog.class, blogId);
            if (blog != null) {
                blog.setViewCount(blog.getViewCount() + 1);
                entityManager.merge(blog);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<Blog> getTopViewedBlogs(int limit) {
        TypedQuery<Blog> query = entityManager.createQuery(
                "SELECT b FROM Blog b ORDER BY b.viewCount DESC", Blog.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Blog> getTopViewedBlogs() {
        TypedQuery<Blog> query = entityManager.createQuery(
                "SELECT b FROM Blog b ORDER BY b.viewCount DESC", Blog.class);
        return query.getResultList();
    }

    public List<Blog> getTopNewestBlogs(int limit) {
        TypedQuery<Blog> query = entityManager.createQuery(
                "SELECT b FROM Blog b ORDER BY b.createdAt DESC", Blog.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Blog> getTopNewestBlogs() {
        TypedQuery<Blog> query = entityManager.createQuery(
                "SELECT b FROM Blog b ORDER BY b.createdAt DESC", Blog.class);
        return query.getResultList();
    }
}
