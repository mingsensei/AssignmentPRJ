package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import org.example.rf.model.TestAttempt;

public class TestAttemptDAO extends GenericDAO<TestAttempt, Long> {

    public TestAttemptDAO(EntityManager entityManager) {
        super(entityManager, TestAttempt.class);
    }

    public long countByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT COUNT(t) FROM TestAttempt t WHERE t.userId = :userId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
