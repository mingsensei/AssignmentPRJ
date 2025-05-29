package org.example.rf.dao;

import org.example.rf.model.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PaymentDAO {

    private final EntityManager entityManager;

    public PaymentDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Payment payment) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(payment);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Payment findById(Long id) {
        return entityManager.find(Payment.class, id);
    }

    public void update(Payment payment) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(payment);
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
            Payment payment = entityManager.find(Payment.class, id);
            if (payment != null) {
                entityManager.remove(payment);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<Payment> findAll() {
        TypedQuery<Payment> query = entityManager.createQuery("SELECT p FROM Payment p", Payment.class);
        return query.getResultList();
    }

    public List<Payment> findByOrderId(Long orderId) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p WHERE p.order.id = :orderId", Payment.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
}
