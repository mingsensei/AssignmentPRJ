package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Cart;
import org.example.rf.model.User;

import java.util.List;

public class CartDAO {

    private final EntityManager entityManager;

    public CartDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Tạo mới Cart
    public void create(Cart cart) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(cart);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Tìm Cart theo id
    public Cart findById(Long id) {
        return entityManager.find(Cart.class, id);
    }

    // Cập nhật Cart
    public void update(Cart cart) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(cart);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Xóa Cart theo id
    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Cart cart = entityManager.find(Cart.class, id);
            if (cart != null) {
                entityManager.remove(cart);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả Cart
    public List<Cart> findAll() {
        TypedQuery<Cart> query = entityManager.createQuery("SELECT c FROM Cart c", Cart.class);
        return query.getResultList();
    }

    // Tìm Cart theo User
    public List<Cart> findByUser(User user) {
        TypedQuery<Cart> query = entityManager.createQuery(
                "SELECT c FROM Cart c WHERE c.user = :user", Cart.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
}
