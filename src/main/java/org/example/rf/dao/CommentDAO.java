package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.example.rf.model.Comment;

public class CommentDAO extends GenericDAO<Comment, Long> {

    public CommentDAO(EntityManager em) {
        super(em, Comment.class);
    }

    public List<Comment> findByUserId(Long userId) {
        return entityManager.createQuery(
                "SELECT c FROM Comment c WHERE c.user.id = :userId", Comment.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
