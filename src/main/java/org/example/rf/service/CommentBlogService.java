package org.example.rf.service;

import org.example.rf.dao.CommentBlogDAO;
import org.example.rf.model.CommentBlog;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;
import org.example.rf.model.Comment;


public class CommentBlogService {
    private final EntityManager em;
    private final CommentBlogDAO commentBlogDAO;

    public CommentBlogService() {
        this.em = JPAUtil.getEntityManager();
        this.commentBlogDAO = new CommentBlogDAO(em);
    }

    public void create(CommentBlog commentBlog) {
        commentBlogDAO.create(commentBlog);
    }

    public List<CommentBlog> findByBlogId(Long blogId) {
        return commentBlogDAO.findByBlogId(blogId);
    }
    
    public List<Comment> findCommentsByBlogId(Long blogId) {
        return commentBlogDAO.findCommentsByBlogId(blogId);
    }

    public void close() {
        if (em != null && em.isOpen()) em.close();
    }
}
