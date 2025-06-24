package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import org.example.rf.model.BlogUser;

public class BlogUserDAO extends GenericDAO<BlogUser, Long> {
    public BlogUserDAO(EntityManager entityManager) {
        super(entityManager, BlogUser.class);
    }
}
