package org.example.rf.dao;

import org.example.rf.model.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class OrderItemDAO {

    private final EntityManager entityManager;

    public OrderItemDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(OrderItem orderItem) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(orderItem);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public OrderItem findById(Long id) {
        return entityManager.find(OrderItem.class, id);
    }

    public void update(OrderItem orderItem) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(orderItem);
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
            OrderItem orderItem = entityManager.find(OrderItem.class, id);
            if (orderItem != null) {
                entityManager.remove(orderItem);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<OrderItem> findAll() {
        TypedQuery<OrderItem> query = entityManager.createQuery("SELECT oi FROM OrderItem oi", OrderItem.class);
        return query.getResultList();
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        TypedQuery<OrderItem> query = entityManager.createQuery(
                "SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId", OrderItem.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public List<OrderItem> findByCourseId(Long courseId) {
        TypedQuery<OrderItem> query = entityManager.createQuery(
                "SELECT oi FROM OrderItem oi WHERE oi.course.id = :courseId", OrderItem.class);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }
}
