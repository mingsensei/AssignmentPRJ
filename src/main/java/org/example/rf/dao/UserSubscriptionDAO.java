package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import org.example.rf.model.UserSubscription;

import java.util.List;

public class UserSubscriptionDAO extends GenericDAO<UserSubscription, Long> {

    public UserSubscriptionDAO(EntityManager entityManager) {
        super(entityManager, UserSubscription.class);
    }

    public UserSubscription findActiveByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT us FROM UserSubscription us WHERE us.userId = :userId " +
                                "AND (us.endDate IS NULL OR us.endDate > CURRENT_DATE)", UserSubscription.class)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getSingleResult();
    }

    public List<UserSubscription> findByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT us FROM UserSubscription us WHERE us.userId = :userId", UserSubscription.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
