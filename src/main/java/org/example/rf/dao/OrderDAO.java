package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.model.Order;
import org.example.rf.util.JPAUtil;

import java.math.BigDecimal;
import java.util.List;

public class OrderDAO {

    // Không còn nhận EntityManager từ constructor
    public OrderDAO() {
    }

    public Order findPendingOrderByUserId(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Order> query = em.createQuery(
                    "SELECT o FROM Order o WHERE o.user.id = :userId AND o.status = 'pending' ORDER BY o.createdAt DESC",
                    Order.class
            );
            query.setParameter("userId", userId);
            query.setMaxResults(1);

            List<Order> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    public Order create(Order order) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Order update(Order order) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Order updatedOrder = em.merge(order);
            transaction.commit();
            return updatedOrder;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Order findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Order.class, id);
        } finally {
            em.close();
        }
    }

    public List<Order> findByUserId(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Order> query = em.createQuery(
                    "SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC",
                    Order.class
            );
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Order order = em.find(Order.class, id);
            if (order != null) {
                em.remove(order);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Order> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Yêu cầu Hibernate lấy luôn dữ liệu của User và Plan (nếu có)
            TypedQuery<Order> query = em.createQuery(
                    "SELECT o FROM Order o " +
                            "JOIN FETCH o.user " +
                            "LEFT JOIN FETCH o.plan " + // Lấy cả thông tin plan (nếu có)
                            "ORDER BY o.id DESC",
                    Order.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long countTotalParticipants() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(DISTINCT o.user.id) FROM Order o WHERE o.status = 'COMPLETED'",
                    Long.class
            );
            Long count = query.getSingleResult();
            return count == null ? 0L : count;
        } finally {
            em.close();
        }
    }

    public BigDecimal calculateTotalRevenueFromPlans() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<BigDecimal> query = em.createQuery(
                    "SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderType = 'PLAN_PURCHASE' AND o.status = 'Completed'",
                    BigDecimal.class
            );
            BigDecimal total = query.getSingleResult();
            return total == null ? BigDecimal.ZERO : total;
        } finally {
            em.close();
        }
    }

    /**
     * Đếm tổng số đơn hàng MUA PLAN đã hoàn thành.
     */
    public long countCompletedPlanOrders() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(o.id) FROM Order o WHERE o.orderType = 'PLAN_PURCHASE' AND o.status = 'Completed'",
                    Long.class
            );
            Long count = query.getSingleResult();
            return count == null ? 0L : count;
        } finally {
            em.close();
        }
    }
}