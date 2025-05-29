package org.example.rf.service;

import org.example.rf.dao.MaterialDAO;
import org.example.rf.model.Material;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class MaterialService {

    private final EntityManager em;
    private final MaterialDAO materialDAO;

    public MaterialService() {
        this.em = JPAUtil.getEntityManager();  // Tạo EntityManager 1 lần
        this.materialDAO = new MaterialDAO(em); // Tạo DAO 1 lần với EntityManager đó
    }

    // Tạo mới Material
    public void createMaterial(Material material) {
        materialDAO.create(material);
    }

    // Tìm Material theo ID
    public Material getMaterialById(Long id) {
        return materialDAO.findById(id);
    }

    // Cập nhật Material
    public void updateMaterial(Material material) {
        materialDAO.update(material);
    }

    // Xóa Material theo ID
    public void deleteMaterial(Long id) {
        materialDAO.delete(id);
    }

    // Lấy danh sách tất cả Material
    public List<Material> getAllMaterials() {
        return materialDAO.findAll();
    }

    // Đóng EntityManager khi không còn sử dụng
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
