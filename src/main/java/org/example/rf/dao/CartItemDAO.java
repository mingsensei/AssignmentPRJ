package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Cart;
import org.example.rf.model.CartItem;
import org.example.rf.model.Course;
import java.util.List;

public class CartItemDAO extends GenericDAO<CartItem, Long> {
    public CartItemDAO(EntityManager entityManager) {
        super(entityManager, CartItem.class);
    }

    public List<CartItem> findByCart(Cart cart) {
        TypedQuery<CartItem> query = entityManager.createQuery(
                "SELECT ci FROM CartItem ci WHERE ci.cart = :cart", CartItem.class);
        query.setParameter("cart", cart);
        return query.getResultList();
    }

    public List<CartItem> findByCourse(Course course) {
        TypedQuery<CartItem> query = entityManager.createQuery(
                "SELECT ci FROM CartItem ci WHERE ci.course = :course", CartItem.class);
        query.setParameter("course", course);
        return query.getResultList();
    }
}
