package org.example.rf.dao;

import org.example.rf.model.Material;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class MaterialDAO {

    private final EntityManager entityManager;

    public MaterialDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Material material) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(material);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Material findById(Long id) {
        return entityManager.find(Material.class, id);
    }

    public void update(Material material) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(material);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Material material = entityManager.find(Material.class, id);
            if (material != null) {
                entityManager.remove(material);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<Material> findAll() {
        TypedQuery<Material> query = entityManager.createQuery("SELECT m FROM Material m", Material.class);
        return query.getResultList();
    }

    public List<Material> findAllByChapterId(Long chapterId) {
        TypedQuery<Material> query = entityManager.createQuery(
                "SELECT m FROM Material m WHERE m.chapterId = :chapterId", Material.class);
        query.setParameter("chapterId", chapterId);
        return query.getResultList();
    }

    public List<Material> findByType(String type) {
        TypedQuery<Material> query = entityManager.createQuery(
                "SELECT m FROM Material m WHERE m.type = :type", Material.class);
        query.setParameter("type", type);
        return query.getResultList();
    }

    public List<Material> findByChapterIdAndType(Long chapterId, String type) {
        TypedQuery<Material> query = entityManager.createQuery(
                "SELECT m FROM Material m WHERE m.chapterId = :chapterId AND m.type = :type", Material.class);
        query.setParameter("chapterId", chapterId);
        query.setParameter("type", type);
        return query.getResultList();
    }
}
