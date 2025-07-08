package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.example.rf.model.Comment;
import org.example.rf.model.CommentBlog;

public class CommentBlogDAO extends GenericDAO<CommentBlog, Long> {

    public CommentBlogDAO(EntityManager em) {
        super(em, CommentBlog.class);
    }

    public List<Comment> findCommentsByBlogId(Long blogId) {
        return entityManager.createQuery(
                "SELECT cb.comment FROM CommentBlog cb WHERE cb.blog.id = :blogId ORDER BY cb.comment.createdAt DESC",
                Comment.class
        ).setParameter("blogId", blogId)
                .getResultList();
    }

    public List<CommentBlog> findByBlogId(Long blogId) {
        return entityManager.createQuery(
                "SELECT cb FROM CommentBlog cb WHERE cb.blog.id = :blogId", CommentBlog.class)
                .setParameter("blogId", blogId)
                .getResultList();
    }
}
