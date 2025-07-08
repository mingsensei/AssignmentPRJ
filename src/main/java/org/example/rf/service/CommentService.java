package org.example.rf.service;

import org.example.rf.dao.CommentDAO;
import org.example.rf.model.Comment;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;


public class CommentService {

    private final EntityManager em;
    private final CommentDAO commentDAO;

    public CommentService() {
        this.em = JPAUtil.getEntityManager();
        this.commentDAO = new CommentDAO(em);
    }

    public void create(Comment comment) {
        commentDAO.create(comment);
    }

    public Comment findById(Long id) {
        return commentDAO.findById(id);
    }

    public void update(Comment comment) {
        commentDAO.update(comment);
    }

    public void delete(Long id) {
        commentDAO.delete(id);
    }

    public List<Comment> getAll() {
        return commentDAO.findAll();
    }

    public List<Comment> findByUserId(Long userId) {
        return commentDAO.findByUserId(userId);
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
