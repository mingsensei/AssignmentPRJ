package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import org.example.rf.model.UserPost;

public class UserPostDAO extends GenericDAO<UserPost, Long> {

    public UserPostDAO(EntityManager entityManager) {
        super(entityManager, UserPost.class);
    }

    public long countByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT COUNT(p) FROM UserPost p WHERE p.userId = :userId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
