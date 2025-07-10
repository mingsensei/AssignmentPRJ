package org.example.rf.service;

import org.example.rf.dao.CommentLessonDAO;
import org.example.rf.model.CommentLesson;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;
import org.example.rf.model.Comment;

public class CommentLessonService {
    private final EntityManager em;
    private final CommentLessonDAO commentLessonDAO;

    public CommentLessonService() {
        this.em = JPAUtil.getEntityManager();
        this.commentLessonDAO = new CommentLessonDAO(em);
    }

    public void create(CommentLesson commentLesson) {
        commentLessonDAO.create(commentLesson);
    }

    public List<CommentLesson> findByLessonId(Long lessonId) {
        return commentLessonDAO.findByLessonId(lessonId);
    }
    
    public List<Comment> findCommentsByLessonId(Long lessonId) {
        return commentLessonDAO.findCommentsByLessonId(lessonId);
    }

    public void close() {
        if (em != null && em.isOpen()) em.close();
    }
}
