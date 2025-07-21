package org.example.rf.service;

import org.example.rf.dao.OrderDAO;
import org.example.rf.dao.PaymentDAO;
import org.example.rf.dao.PlanDAO;
import org.example.rf.dao.UserDAO;
import org.example.rf.model.Order;
import org.example.rf.model.Payment;
import org.example.rf.model.Plan;
import org.example.rf.model.User;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentService {

    private final EntityManager em;
    private final PaymentDAO paymentDAO;
    private final OrderDAO orderDAO;
    private final PlanDAO planDAO;
    private final UserDAO userDAO;

    public PaymentService() {
        this.em = JPAUtil.getEntityManager();  // tạo EntityManager 1 lần
        this.paymentDAO = new PaymentDAO(em);  // tạo DAO 1 lần với EntityManager đó
        this.orderDAO = new OrderDAO();
        this.planDAO = new PlanDAO(em);
        this.userDAO = new UserDAO(em);
    }

    // Tạo mới payment
    public void createPayment(Payment payment) {
        paymentDAO.create(payment);
    }

    // Tìm payment theo ID
    public Payment getPaymentById(Long id) {
        return paymentDAO.findById(id);
    }

    // Cập nhật payment
    public void updatePayment(Payment payment) {
        paymentDAO.update(payment);
    }

    // Xóa payment theo ID
    public void deletePayment(Long id) {
        paymentDAO.delete(id);
    }

    // Lấy danh sách tất cả payments
    public List<Payment> getAllPayments() {
        return paymentDAO.findAll();
    }

    // Đóng EntityManager khi không dùng nữa
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
    // Lấy danh sách các payment theo userId
    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentDAO.findByUserId(userId);
    }


    public void createPaymentForPlan(Long userId, Long planId, BigDecimal amount, String bankCode) {
        try {
            Plan plan = planDAO.findById(planId);
            if (plan == null) {
                throw new IllegalArgumentException("Không tìm thấy gói dịch vụ với ID: " + planId);
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                throw new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId);
            }

            Payment payment = new Payment();
            payment.setUser(user);
            payment.setAmount(amount);
            payment.setTransactionNo("PLAN-TXN-" + System.currentTimeMillis());
            payment.setBankCode(bankCode);
            payment.setPayDate(LocalDateTime.now());
            payment.setStatus("PENDING");
            payment.setCreatedAt(LocalDateTime.now());

            paymentDAO.create(payment);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo payment cho plan: " + e.getMessage(), e);
        }
    }

    public void createPaymentForOrder(Long userId, Long orderId, BigDecimal amount, String bankCode) {
        try {
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                throw new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + orderId);
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                throw new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId);
            }

            Payment payment = new Payment();
            payment.setUser(user);
            payment.setOrder(order);
            payment.setAmount(amount);
            payment.setTransactionNo("TXN-" + System.currentTimeMillis());
            payment.setBankCode(bankCode);
            payment.setPayDate(LocalDateTime.now());
            payment.setStatus("PENDING");
            payment.setCreatedAt(LocalDateTime.now());

            paymentDAO.create(payment);
            order.setStatus("PAID");
            orderDAO.update(order);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo payment cho order: " + e.getMessage(), e);
        }
    }

    public List<Payment> getOrderPaymentsByUserId(Long userId) {
        return paymentDAO.findOrderPaymentsByUserId(userId);
    }

    public List<Payment> getPlanPaymentsByUserId(Long userId) {
        return paymentDAO.findPlanPaymentsByUserId(userId);
    }

}
