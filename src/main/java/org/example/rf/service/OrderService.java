package org.example.rf.service;

// Bỏ import EntityManager không cần thiết
import org.example.rf.dao.OrderDAO;
import org.example.rf.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final OrderDAO orderDAO;
    // Bỏ trường EntityManager
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final EnrollmentService enrollmentService;

    public OrderService() {
        // Không tạo EntityManager ở đây
        this.enrollmentService = new EnrollmentService();
        this.orderDAO = new OrderDAO(); // Khởi tạo DAO không cần tham số
        this.orderItemService = new OrderItemService();
        this.userService = new UserService();
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

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }


    public List<OrderItem> getOrderItemsByUserId(Long userId) {
        Order pendingOrder = orderDAO.findPendingOrderByUserId(userId);
        if (pendingOrder == null) {
            return new ArrayList<>();
        }
        return orderItemService.getOrderItemsByOrderId(pendingOrder.getId());
    }

    public Order findPendingOrderByUserId(Long userId) {
        return orderDAO.findPendingOrderByUserId(userId);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderDAO.findByUserId(userId);
    }

    public BigDecimal calculateTotalAmount(Long orderId) {
        // Vì DAO bây giờ tự quản lý EntityManager, logic này vẫn hoạt động đúng
        List<OrderItem> items = orderItemService.getOrderItemsByOrderId(orderId);
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : items) {
            if (item.getPrice() != null) {
                total = total.add(item.getPrice());
            }
        }
        Order order = this.getOrderById(orderId);
        if (order != null) {
            order.setTotalAmount(total);
            orderDAO.update(order);
        }
        return total;
    }

    public void updateOrderStatusByVnpayReturn(String transactionStatus, Long orderIdLong) {
        Order order = this.getOrderById(orderIdLong);
        if (order == null) return; // Luôn kiểm tra null

        if ("00".equals(transactionStatus)) {
            order.setStatus("Completed");
        } else {
            order.setStatus("Failed");
        }
        this.updateOrder(order); // Dùng phương thức của service

        if ("Completed".equals(order.getStatus())) {
            List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderIdLong);
            List<Course> courses = new ArrayList<>();
            for (OrderItem orderItem : orderItems) {
                courses.add(orderItem.getCourse());
            }
            enrollmentService.createEnrollment(order.getUser(), courses);
        }
    }

    public Long createNewOrderByVnpay(Long userId, double amountDouble, Map<Long, Course> cart) {
        User userFind = userService.getUserById(userId);
        Order order = Order.builder()
                .user(userFind)
                .totalAmount(BigDecimal.valueOf(amountDouble))
                .status("Pending")
                .createdAt(LocalDateTime.now())
                .build();

        order = this.createOrder(order); // Dùng phương thức của service

        if (order != null) {
            for (Map.Entry<Long, Course> entry : cart.entrySet()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setCourse(entry.getValue());
                orderItem.setPrice(BigDecimal.valueOf(entry.getValue().getPrice()));
                orderItemService.createOrderItem(orderItem);
            }
            return order.getId();
        }
        return null;
    }
}