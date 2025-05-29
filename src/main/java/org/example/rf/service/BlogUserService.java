package org.example.rf.service;

import org.example.rf.dao.BlogUserDAO;
import org.example.rf.model.BlogUser;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class BlogUserService {

    private final EntityManager em;
    private final BlogUserDAO blogUserDAO;

    public BlogUserService() {
        this.em = JPAUtil.getEntityManager();        // Tạo EM 1 lần
        this.blogUserDAO = new BlogUserDAO(em);      // Tạo DAO 1 lần
    }

    // Tạo blog-user mới
    public void createBlogUser(BlogUser blogUser) {
        blogUserDAO.create(blogUser);
    }

    // Tìm blog-user theo ID
    public BlogUser getBlogUserById(Long id) {
        return blogUserDAO.findById(id);
    }

    // Cập nhật blog-user
    public void updateBlogUser(BlogUser blogUser) {
        blogUserDAO.update(blogUser);
    }

    // Xóa blog-user theo ID
    public void deleteBlogUser(Long id) {
        blogUserDAO.delete(id);
    }

    // Lấy danh sách tất cả blog-user
    public List<BlogUser> getAllBlogUsers() {
        return blogUserDAO.findAll();
    }

    // Dọn dẹp EntityManager
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
