package org.example.rf.service;

// Bỏ import EntityManager không cần thiết
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import org.example.rf.dao.OrderDAO;
import org.example.rf.model.*;
import org.example.rf.util.JPAUtil;

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

    public void updateOrderStatusByVnpayReturn(String transactionStatus, Long orderId) {
        if ("00".equals(transactionStatus)) {
            boolean success = processSuccessfulPurchase(orderId);
            if (!success) {
                System.err.println("CRITICAL: Payment success for Order " + orderId + " but processing failed (out of stock). Manual refund needed.");
            }
        } else {
            processFailedPurchase(orderId);
        }
    }

    private boolean processSuccessfulPurchase(Long orderId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Order order = em.find(Order.class, orderId);
            if (order == null) {
                tx.rollback();
                return false;
            }

            List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);

            for (OrderItem item : orderItems) {
                Course course = em.find(Course.class, item.getCourse().getId(), LockModeType.PESSIMISTIC_WRITE);

                if (course.getQuantity() <= 0) {
                    order.setStatus("Failed_OutOfStock");
                    em.merge(order);
                    tx.rollback();
                    return false;
                }

                course.setQuantity(course.getQuantity() - 1);
                em.merge(course);

                Enrollment enrollment = Enrollment.builder()
                        .user(order.getUser())
                        .course(course)
                        .enrolledAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMonths(6))
                        .build();
                em.persist(enrollment);
            }

            order.setStatus("Completed");
            em.merge(order);

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            try {
                Order order = orderDAO.findById(orderId);
                if (order != null && !"Completed".equals(order.getStatus())) {
                    order.setStatus("Failed_Exception");
                    orderDAO.update(order);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private void processFailedPurchase(Long orderId) {
        Order order = orderDAO.findById(orderId);
        if (order != null && !"Completed".equals(order.getStatus())) {
            order.setStatus("Failed");
            orderDAO.update(order);
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