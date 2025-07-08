package org.example.rf.dao;

import org.example.rf.model.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PaymentDAO extends GenericDAO<Payment, Long> {
    public PaymentDAO(EntityManager entityManager) {
        super(entityManager, Payment.class);
    }

    public List<Payment> findByOrderId(Long orderId) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p WHERE p.order.id = :orderId", Payment.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public List<Payment> findByUserId(Long userId) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p WHERE p.order.user.id = :userId", Payment.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    // Chỉ lấy payment dành cho Order (có order != null)
    public List<Payment> findOrderPaymentsByUserId(Long userId) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p WHERE p.order.user.id = :userId", Payment.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Payment> findPlanPaymentsByUserId(Long userId) {
        TypedQuery<Payment> query = entityManager.createQuery(
                """
                SELECT p FROM Payment p
                WHERE p.order IS NULL
                  AND p.user.id = :userId
                """, Payment.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }


}
