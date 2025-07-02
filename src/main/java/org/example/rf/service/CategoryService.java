package org.example.rf.service;

import org.example.rf.dao.CategoryDAO;
import org.example.rf.model.Category;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private final EntityManager em;
    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.em = JPAUtil.getEntityManager();
        this.categoryDAO = new CategoryDAO(em);
    }

    public void createCategory(Category category) {
        categoryDAO.create(category);
    }

    public Category getCategoryById(Long id) {
        return categoryDAO.findById(id);
    }

    public void updateCategory(Category category) {
        categoryDAO.update(category);
    }

    public void deleteCategory(Long id) {
        categoryDAO.delete(id);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public ArrayList<Category> getTop4Courses() {
        return new ArrayList<>(categoryDAO.getTop4Categories());
    }

    public boolean categoryNameExists(String name, Long excludeId) {
        return categoryDAO.existsByName(name, excludeId);
    }

    public boolean isCategoryInUse(Long categoryId) {
        return categoryDAO.isCategoryInUse(categoryId);
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}