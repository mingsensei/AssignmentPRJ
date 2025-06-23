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

    public OrderItem create(OrderItem orderItem) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(orderItem);
            transaction.commit();
            return orderItem;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public OrderItem findById(Long id) {
        return entityManager.find(OrderItem.class, id);
    }

    public OrderItem update(OrderItem orderItem) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            OrderItem updatedOrderItem = entityManager.merge(orderItem);
            transaction.commit();
            return updatedOrderItem;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            OrderItem orderItem = entityManager.find(OrderItem.class, id);
            if (orderItem != null) {
                entityManager.remove(orderItem);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
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

    public boolean existsByOrderIdAndCourseId(Long orderId, Long courseId) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(oi) FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.course.id = :courseId",
            Long.class
        );
        query.setParameter("orderId", orderId);
        query.setParameter("courseId", courseId);
        
        return query.getSingleResult() > 0;
    }

}
