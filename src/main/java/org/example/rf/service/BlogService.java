package org.example.rf.service;

import org.example.rf.dao.BlogDAO;
import org.example.rf.model.Blog;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

public class BlogService {

    private final EntityManager em;
    private final BlogDAO blogDAO;

    public BlogService() {
        this.em = JPAUtil.getEntityManager();  // chỉ tạo EntityManager 1 lần
        this.blogDAO = new BlogDAO(em);        // tạo DAO 1 lần
    }

    public void createBlog(Blog blog) {
        blogDAO.create(blog);
    }

    public Blog getBlogById(Long id) {
        return blogDAO.findById(id);
    }

    public void updateBlog(Blog blog) {
        blogDAO.update(blog);
    }

    public void deleteBlog(Long id) {
        blogDAO.delete(id);
    }

    public List<Blog> getAllBlogs() {
        return blogDAO.findAll();
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
    
        @Transactional
    public void incrementViewCount(Long blogId) {
        blogDAO.incrementViewCount(blogId);
    }

    public List<Blog> getTopViewedBlogs(int limit) {
        return blogDAO.getTopViewedBlogs(limit);
    }

    public List<Blog> getTopNewestBlogs(int limit) {
        return blogDAO.getTopNewestBlogs(limit);
    }
    
    public List<Blog> getTopViewedBlogs() {
        return blogDAO.getTopViewedBlogs();
    }

    public List<Blog> getTopNewestBlogs() {
        return blogDAO.getTopNewestBlogs();
    }
}
