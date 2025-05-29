package org.example.rf.dao;

import org.example.rf.model.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class OrderDAO {

    private final EntityManager entityManager;

    public OrderDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Order order) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(order);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Order findById(Long id) {
        return entityManager.find(Order.class, id);
    }

    public void update(Order order) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(order);
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
            Order order = entityManager.find(Order.class, id);
            if (order != null) {
                entityManager.remove(order);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<Order> findAll() {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o", Order.class);
        return query.getResultList();
    }

    public List<Order> findByUserId(Long userId) {
        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT o FROM Order o WHERE o.user.id = :userId", Order.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Order> findByStatus(String status) {
        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT o FROM Order o WHERE o.status = :status", Order.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
}
