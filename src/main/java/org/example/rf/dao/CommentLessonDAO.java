package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.example.rf.model.Comment;
import org.example.rf.model.CommentLesson;

public class CommentLessonDAO extends GenericDAO<CommentLesson, Long> {

    public CommentLessonDAO(EntityManager em) {
        super(em, CommentLesson.class);
    }

    public List<Comment> findCommentsByLessonId(Long lessonId) {
        return entityManager.createQuery(
                "SELECT cc.comment FROM CommentLesson cc WHERE cc.lesson.id = :lessonId ORDER BY cc.comment.createdAt DESC",
                Comment.class
        ).setParameter("lessonId", lessonId)
                .getResultList();
    }

    public List<CommentLesson> findByLessonId(Long lessonId) {
        return entityManager.createQuery(
                "SELECT cc FROM CommentLesson cc WHERE cc.lesson.id = :lessonId", CommentLesson.class)
                .setParameter("lessonId", lessonId)
                .getResultList();
    }
}
