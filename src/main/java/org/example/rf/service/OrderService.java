package org.example.rf.service;

import org.example.rf.dao.OrderDAO;
import org.example.rf.model.Order;
import org.example.rf.model.OrderItem;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class OrderService {

    private final EntityManager em;
    private final OrderDAO orderDAO;
    private OrderItemService orderItemService= new OrderItemService();

    public OrderService() {
        this.em = JPAUtil.getEntityManager();  // Tạo EntityManager 1 lần khi khởi tạo service
        this.orderDAO = new OrderDAO(em);      // Tạo DAO 1 lần với EntityManager đó
    }

    // Tạo mới đơn hàng
    public void createOrder(Order order) {
        orderDAO.create(order);
    }

    // Tìm đơn hàng theo ID
    public Order getOrderById(Long id) {
        return orderDAO.findById(id);
    }

    // Cập nhật đơn hàng
    public void updateOrder(Order order) {
        orderDAO.update(order);
    }

    // Xóa đơn hàng theo ID
    public void deleteOrder(Long id) {
        orderDAO.delete(id);
    }

    // Lấy danh sách tất cả đơn hàng
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    // Đóng EntityManager khi không còn dùng nữa
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
    public List<OrderItem> getOrderItemsByUserId(Long userId) {
        List<OrderItem> allItems = new java.util.ArrayList<>();

       allItems =orderItemService.getOrderItemsByOrderId(orderDAO.findByUserId(userId).getId());

        return allItems;
    }
}
