package org.example.rf.service;

import jakarta.persistence.EntityManager;
import org.example.rf.dao.OrderDAO;
import org.example.rf.model.Order;
import org.example.rf.model.OrderItem;
import org.example.rf.util.JPAUtil;

import java.math.BigDecimal;
import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;
    private final EntityManager entityManager;
    private OrderItemService orderItemService;

    public OrderService() {
        this.entityManager = JPAUtil.getEntityManager();
        this.orderDAO = new OrderDAO(entityManager);
        this.orderItemService = new OrderItemService();
    }

    // Tạo mới đơn hàng
    public Order createOrder(Order order) {
        return orderDAO.create(order);
    }

    // Tìm đơn hàng theo ID
    public Order getOrderById(Long id) {
        return orderDAO.findById(id);
    }

    // Cập nhật đơn hàng
    public Order updateOrder(Order order) {
        return orderDAO.update(order);
    }

    // Xóa đơn hàng theo ID
    public void deleteOrder(Long id) {
        orderDAO.delete(id);
    }

    // Lấy danh sách tất cả đơn hàng
    public List<Order> getAllOrders() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return new OrderDAO(em).findAll();
        } finally {
            em.close();
        }
    }

    // Đóng EntityManager khi không còn dùng nữa
    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

    public List<OrderItem> getOrderItemsByUserId(Long userId) {
        // Tìm order pending của user
        Order pendingOrder = orderDAO.findPendingOrderByUserId(userId);
        
        // Nếu không có order pending, trả về list rỗng
        if (pendingOrder == null) {
            return new java.util.ArrayList<>();
        }
        
        // Lấy danh sách order items từ order pending
        return orderItemService.getOrderItemsByOrderId(pendingOrder.getId());
    }

    public Order findPendingOrderByUserId(Long userId) {
        return orderDAO.findPendingOrderByUserId(userId);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderDAO.findByUserId(userId);
    }
    public BigDecimal calculateTotalAmount(Long orderId) {
        List<OrderItem> items = orderItemService.getOrderItemsByOrderId(orderId);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : items) {
            if (item.getPrice() != null) {
                total = total.add(item.getPrice());
            }
        }
        Order order = this.getOrderById(orderId);
        order.setTotalAmount(total);
        orderDAO.update(order);
        return total;
    }
}
