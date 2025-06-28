package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.User;

public class UserDAO extends GenericDAO<User, Long> {
    public UserDAO(EntityManager entityManager) {
        super(entityManager, User.class);
    }

    // Tìm User theo email
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst().orElse(null);
    }

    // Tìm User theo GoogleId
    public User findByGoogleId(String googleId) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.googleId = :googleId", User.class);
        query.setParameter("googleId", googleId);
        return query.getResultStream().findFirst().orElse(null);
    }
}
