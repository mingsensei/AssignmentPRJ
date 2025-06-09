package org.example.rf.service;

import org.example.rf.dao.OrderItemDAO;
import org.example.rf.model.OrderItem;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class OrderItemService {

    private final EntityManager em;
    private final OrderItemDAO orderItemDAO;

    public OrderItemService() {
        this.em = JPAUtil.getEntityManager();  // Tạo EntityManager 1 lần khi khởi tạo service
        this.orderItemDAO = new OrderItemDAO(em); // Tạo DAO 1 lần với EntityManager đó
    }

    // Tạo mới OrderItem
    public void createOrderItem(OrderItem orderItem) {
        orderItemDAO.create(orderItem);
    }

    // Tìm OrderItem theo ID
    public OrderItem getOrderItemById(Long id) {
        return orderItemDAO.findById(id);
    }

    // Cập nhật OrderItem
    public void updateOrderItem(OrderItem orderItem) {
        orderItemDAO.update(orderItem);
    }

    // Xóa OrderItem theo ID
    public void deleteOrderItem(Long id) {
        orderItemDAO.delete(id);
    }

    // Lấy danh sách tất cả OrderItem
    public List<OrderItem> getAllOrderItems() {
        return orderItemDAO.findAll();
    }

    // Đóng EntityManager khi không còn dùng nữa
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemDAO.findByOrderId(orderId);
    }
}
