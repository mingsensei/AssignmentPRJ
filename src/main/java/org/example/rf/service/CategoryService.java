package org.example.rf.service;

import org.example.rf.dao.CategoryDAO;
import org.example.rf.model.Category;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class CategoryService {

    private final EntityManager em;
    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.em = JPAUtil.getEntityManager();  // Tạo EntityManager 1 lần
        this.categoryDAO = new CategoryDAO(em); // Tạo DAO 1 lần dùng chung
    }

    // Tạo category mới
    public void createCategory(Category category) {
        categoryDAO.create(category);
    }

    // Tìm category theo ID
    public Category getCategoryById(Long id) {
        return categoryDAO.findById(id);
    }

    // Cập nhật category
    public void updateCategory(Category category) {
        categoryDAO.update(category);
    }

    // Xóa category theo ID
    public void deleteCategory(Long id) {
        categoryDAO.delete(id);
    }

    // Lấy danh sách tất cả category
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    // Đóng EntityManager khi không cần nữa
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
