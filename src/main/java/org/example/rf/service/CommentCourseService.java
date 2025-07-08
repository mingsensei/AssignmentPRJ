package org.example.rf.service;

import org.example.rf.dao.CommentCourseDAO;
import org.example.rf.model.CommentCourse;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;
import org.example.rf.model.Comment;

public class CommentCourseService {
    private final EntityManager em;
    private final CommentCourseDAO commentCourseDAO;

    public CommentCourseService() {
        this.em = JPAUtil.getEntityManager();
        this.commentCourseDAO = new CommentCourseDAO(em);
    }

    public void create(CommentCourse commentCourse) {
        commentCourseDAO.create(commentCourse);
    }

    public List<CommentCourse> findByCourseId(Long courseId) {
        return commentCourseDAO.findByCourseId(courseId);
    }
    
    public List<Comment> findCommentsByCourseId(Long courseId) {
        return commentCourseDAO.findCommentsByCourseId(courseId);
    }

    public void close() {
        if (em != null && em.isOpen()) em.close();
    }
}
