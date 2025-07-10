package org.example.rf.service;

import org.example.rf.dao.CommentChapterDAO;
import org.example.rf.model.CommentChapter;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;
import org.example.rf.model.Comment;

public class CommentChapterService {
    private final EntityManager em;
    private final CommentChapterDAO commentChapterDAO;

    public CommentChapterService() {
        this.em = JPAUtil.getEntityManager();
        this.commentChapterDAO = new CommentChapterDAO(em);
    }

    public void create(CommentChapter commentChapter) {
        commentChapterDAO.create(commentChapter);
    }

    public List<CommentChapter> findByChapterId(Long chapterId) {
        return commentChapterDAO.findByChapterId(chapterId);
    }
    
    public List<Comment> findCommentsByChapterId(Long chapterId) {
        return commentChapterDAO.findCommentsByChapterId(chapterId);
    }

    public void close() {
        if (em != null && em.isOpen()) em.close();
    }
}
