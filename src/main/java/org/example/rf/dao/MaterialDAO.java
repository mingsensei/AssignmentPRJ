package org.example.rf.dao;

import org.example.rf.model.Material;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.util.JPAUtil; // Đảm bảo bạn đã import lớp tiện ích JPA

import java.util.List;

public class MaterialDAO {

    public MaterialDAO() {
        // Constructor không cần thay đổi
    }

    public void create(Material material) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(material);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close(); // Luôn đóng EntityManager
        }
    }

    public Material findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Material.class, id);
        } finally {
            em.close();
        }
    }

    public void update(Material material) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(material);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Material material = em.find(Material.class, id);
            if (material != null) {
                em.remove(material);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Material> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m ORDER BY m.id DESC", Material.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Material> findAllByChapterId(Long chapterId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Material> query = em.createQuery(
                    "SELECT m FROM Material m WHERE m.chapter.id = :chapterId", Material.class);
            query.setParameter("chapterId", chapterId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Material> findByType(String type) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Material> query = em.createQuery(
                    "SELECT m FROM Material m WHERE m.type = :type", Material.class);
            query.setParameter("type", type);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Material> findByChapterIdAndType(Long chapterId, String type) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Material> query = em.createQuery(
                    "SELECT m FROM Material m WHERE m.chapter.id = :chapterId AND m.type = :type", Material.class);
            query.setParameter("chapterId", chapterId);
            query.setParameter("type", type);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}