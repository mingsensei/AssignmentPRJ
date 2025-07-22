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
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final EnrollmentService enrollmentService;
    private final UserSubscriptionService userSubscriptionService;
    public OrderService() {
        // Không tạo EntityManager ở đây
        this.enrollmentService = new EnrollmentService();
        this.orderDAO = new OrderDAO(); // Khởi tạo DAO không cần tham số
        this.orderItemService = new OrderItemService();
        this.userService = new UserService();
        this.userSubscriptionService = new UserSubscriptionService();
    }

    public Order createOrder(Order order) {
        return orderDAO.create(order);
    }

    public Order getOrderById(Long id) {
        return orderDAO.findById(id);
    }

    public Order updateOrder(Order order) {
        return orderDAO.update(order);
    }

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
        if (order == null) return;

        if ("00".equals(transactionStatus)) {
            order.setStatus("Completed");
        } else {
            order.setStatus("Failed");
        }
        this.updateOrder(order);

        if ("Completed".equals(order.getStatus())) {
            if ("COURSE_PURCHASE".equals(order.getOrderType())) {
                List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderIdLong);
                List<Course> courses = new ArrayList<>();
                for (OrderItem orderItem : orderItems) {
                    courses.add(orderItem.getCourse());
                }
                enrollmentService.createEnrollment(order.getUser(), courses);
            }
            else if ("PLAN_PURCHASE".equals(order.getOrderType())) {
                if (order.getPlan() != null) {
                    userSubscriptionService.updateUserScrition(order.getUser(), order.getPlan());
                }
            }
        }
    }

    public Long createNewOrderByVnpay(Long userId, double amountDouble, Map<Long, Course> cart) {
        User userFind = userService.getUserById(userId);
        Order order = Order.builder()
                .user(userFind)
                .totalAmount(BigDecimal.valueOf(amountDouble))
                .status("Pending")
                .createdAt(LocalDateTime.now())
                .orderType("COURSE_PURCHASE") // Gán tường minh loại order
                .build();

        order = this.createOrder(order);

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

    public Long createNewPlanOrder(User user, Plan plan) {
        if (user == null || plan == null) {
            return null;
        }

        Order order = Order.builder()
                .user(user)
                .totalAmount(plan.getPrice()) // Lấy giá từ Plan
                .status("Pending")
                .createdAt(LocalDateTime.now())
                .orderType("PLAN_PURCHASE") // Gán loại order là PLAN
                .plan(plan) // Gán plan được mua vào order
                .build();

        order = this.createOrder(order);
        // Order mua Plan không có OrderItem
        return (order != null) ? order.getId() : null;
    }
}