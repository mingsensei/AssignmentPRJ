package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.example.rf.model.Comment;
import org.example.rf.model.CommentChapter;

public class CommentChapterDAO extends GenericDAO<CommentChapter, Long> {

    public CommentChapterDAO(EntityManager em) {
        super(em, CommentChapter.class);
    }

    public List<Comment> findCommentsByChapterId(Long chapterId) {
        return entityManager.createQuery(
                "SELECT cc.comment FROM CommentChapter cc WHERE cc.chapter.id = :chapterId ORDER BY cc.comment.createdAt DESC",
                Comment.class
        ).setParameter("chapterId", chapterId)
                .getResultList();
    }

    public List<CommentChapter> findByChapterId(Long chapterId) {
        return entityManager.createQuery(
                "SELECT cc FROM CommentChapter cc WHERE cc.chapter.id = :chapterId", CommentChapter.class)
                .setParameter("chapterId", chapterId)
                .getResultList();
    }
}
