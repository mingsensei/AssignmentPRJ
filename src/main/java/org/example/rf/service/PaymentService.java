package org.example.rf.service;

import org.example.rf.dao.PaymentDAO;
import org.example.rf.model.Payment;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class PaymentService {

    private final EntityManager em;
    private final PaymentDAO paymentDAO;

    public PaymentService() {
        this.em = JPAUtil.getEntityManager();  // tạo EntityManager 1 lần
        this.paymentDAO = new PaymentDAO(em);  // tạo DAO 1 lần với EntityManager đó
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
}
