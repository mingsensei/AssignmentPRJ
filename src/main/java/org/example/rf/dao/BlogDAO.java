package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import org.example.rf.model.Blog;

public class BlogDAO extends GenericDAO<Blog, Long> {
    public BlogDAO(EntityManager entityManager) {
        super(entityManager, Blog.class);
    }
}
