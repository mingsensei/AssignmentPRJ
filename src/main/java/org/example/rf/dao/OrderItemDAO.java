package org.example.rf.dao;

import org.example.rf.model.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
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

    public BigDecimal calculateTotalRevenue() {
        // JPQL này tính tổng giá của tất cả OrderItem thuộc về các Order có status là 'COMPLETED'
        TypedQuery<BigDecimal> query = entityManager.createQuery(
                "SELECT SUM(oi.price) FROM OrderItem oi WHERE oi.order.status = 'COMPLETED'",
                BigDecimal.class
        );
        BigDecimal total = query.getSingleResult();
        return total == null ? BigDecimal.ZERO : total; // Trả về 0 nếu chưa có doanh thu
    }

    // Đếm tổng số khóa học đã bán (tổng số OrderItem trong các đơn hàng đã hoàn thành)
    public long countAllSoldItems() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(oi.id) FROM OrderItem oi WHERE oi.order.status = 'COMPLETED'",
                Long.class
        );
        Long count = query.getSingleResult();
        return count == null ? 0L : count;
    }

    public List<Object[]> getMonthlyRevenueForYear(int year) {
        TypedQuery<Object[]> query = entityManager.createQuery(
                "SELECT FUNCTION('MONTH', o.createdAt), SUM(oi.price) " +
                        "FROM OrderItem oi JOIN oi.order o " +
                        "WHERE o.status = 'COMPLETED' AND FUNCTION('YEAR', o.createdAt) = :year " +
                        "GROUP BY FUNCTION('MONTH', o.createdAt) " +
                        "ORDER BY FUNCTION('MONTH', o.createdAt) ASC",
                Object[].class
        );
        query.setParameter("year", year);
        return query.getResultList();
    }

    public List<Object[]> getRevenuePerCourse() {
        TypedQuery<Object[]> query = entityManager.createQuery(
                "SELECT c.name, SUM(oi.price) " +
                        "FROM OrderItem oi JOIN oi.course c " +
                        "WHERE oi.order.status = 'COMPLETED' " +
                        "GROUP BY c.name " +
                        "ORDER BY SUM(oi.price) DESC", // Sắp xếp giảm dần quan trọng nhất
                Object[].class
        );
        return query.getResultList();
    }

}
