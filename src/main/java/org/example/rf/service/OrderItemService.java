package org.example.rf.service;

import jakarta.persistence.EntityManager;
import org.example.rf.dao.OrderItemDAO;
import org.example.rf.model.OrderItem;
import org.example.rf.util.JPAUtil;

import java.util.List;

public class OrderItemService {
    private final OrderItemDAO orderItemDAO;
    private final EntityManager entityManager;

    public OrderItemService() {
        this.entityManager = JPAUtil.getEntityManager();
        this.orderItemDAO = new OrderItemDAO(entityManager);
    }

    // Tạo mới order item
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemDAO.create(orderItem);
    }

    // Tìm order item theo ID
    public OrderItem getOrderItemById(Long id) {
        return orderItemDAO.findById(id);
    }

    // Cập nhật order item
    public OrderItem updateOrderItem(OrderItem orderItem) {
        return orderItemDAO.update(orderItem);
    }

    // Xóa order item theo ID
    public void deleteOrderItem(Long id) {
        orderItemDAO.delete(id);
    }

    // Lấy danh sách tất cả order items
    public List<OrderItem> getAllOrderItems() {
        return orderItemDAO.findAll();
    }

    // Lấy danh sách order items theo order ID
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemDAO.findByOrderId(orderId);
    }

    // Kiểm tra xem course đã tồn tại trong order chưa
    public boolean isCourseInOrder(Long orderId, Long courseId) {
        return orderItemDAO.existsByOrderIdAndCourseId(orderId, courseId);
    }
    // Kiểm tra xem course đã tồn tại trong order chưa (alias cho isCourseInOrder)
    public boolean existsByOrderIdAndCourseId(Long orderId, Long courseId) {
        return isCourseInOrder(orderId, courseId);
    }
    // Đóng EntityManager khi không còn dùng nữa
    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
