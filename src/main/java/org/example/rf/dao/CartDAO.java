package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Cart;
import org.example.rf.model.User;
import java.util.List;

public class CartDAO extends GenericDAO<Cart, Long> {
    public CartDAO(EntityManager entityManager) {
        super(entityManager, Cart.class);
    }

    public List<Cart> findByUser(User user) {
        TypedQuery<Cart> query = entityManager.createQuery(
                "SELECT c FROM Cart c WHERE c.user = :user", Cart.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
}
