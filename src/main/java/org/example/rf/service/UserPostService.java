package org.example.rf.service;

import org.example.rf.dao.UserPostDAO;
import org.example.rf.model.UserPost;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class UserPostService {

    private final EntityManager em;
    private final UserPostDAO userPostDAO;

    public UserPostService() {
        this.em = JPAUtil.getEntityManager();
        this.userPostDAO = new UserPostDAO(em);
    }

    public void createPost(UserPost post) {
        userPostDAO.create(post);
    }

    public UserPost getPostById(Long id) {
        return userPostDAO.findById(id);
    }

    public void updatePost(UserPost post) {
        userPostDAO.update(post);
    }

    public void deletePost(Long id) {
        userPostDAO.delete(id);
    }

    public List<UserPost> getAllPosts() {
        return userPostDAO.findAll();
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
