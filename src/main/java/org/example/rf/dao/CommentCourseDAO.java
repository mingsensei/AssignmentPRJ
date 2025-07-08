package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.example.rf.model.Comment;
import org.example.rf.model.CommentCourse;

public class CommentCourseDAO extends GenericDAO<CommentCourse, Long> {

    public CommentCourseDAO(EntityManager em) {
        super(em, CommentCourse.class);
    }

    public List<Comment> findCommentsByCourseId(Long courseId) {
        return entityManager.createQuery(
                "SELECT cc.comment FROM CommentCourse cc WHERE cc.course.id = :courseId ORDER BY cc.comment.createdAt DESC",
                Comment.class
        ).setParameter("courseId", courseId)
                .getResultList();
    }

    public List<CommentCourse> findByCourseId(Long courseId) {
        return entityManager.createQuery(
                "SELECT cc FROM CommentCourse cc WHERE cc.course.id = :courseId", CommentCourse.class)
                .setParameter("courseId", courseId)
                .getResultList();
    }
}
