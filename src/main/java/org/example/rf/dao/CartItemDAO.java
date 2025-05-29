package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Cart;
import org.example.rf.model.CartItem;
import org.example.rf.model.Course;

import java.util.List;

public class CartItemDAO {

    private final EntityManager entityManager;

    public CartItemDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Tạo mới CartItem
    public void create(CartItem cartItem) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(cartItem);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Tìm CartItem theo id
    public CartItem findById(Long id) {
        return entityManager.find(CartItem.class, id);
    }

    // Cập nhật CartItem
    public void update(CartItem cartItem) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(cartItem);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Xóa CartItem theo id
    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            CartItem cartItem = entityManager.find(CartItem.class, id);
            if (cartItem != null) {
                entityManager.remove(cartItem);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả CartItem
    public List<CartItem> findAll() {
        TypedQuery<CartItem> query = entityManager.createQuery("SELECT ci FROM CartItem ci", CartItem.class);
        return query.getResultList();
    }

    // Lấy danh sách CartItem theo Cart
    public List<CartItem> findByCart(Cart cart) {
        TypedQuery<CartItem> query = entityManager.createQuery(
                "SELECT ci FROM CartItem ci WHERE ci.cart = :cart", CartItem.class);
        query.setParameter("cart", cart);
        return query.getResultList();
    }

    // Lấy danh sách CartItem theo Course
    public List<CartItem> findByCourse(Course course) {
        TypedQuery<CartItem> query = entityManager.createQuery(
                "SELECT ci FROM CartItem ci WHERE ci.course = :course", CartItem.class);
        query.setParameter("course", course);
        return query.getResultList();
    }
}
