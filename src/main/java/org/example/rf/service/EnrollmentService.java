package org.example.rf.service;

import org.example.rf.dao.EnrollmentDAO;
import org.example.rf.model.Enrollment;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class EnrollmentService {

    private final EntityManager em;
    private final EnrollmentDAO enrollmentDAO;

    public EnrollmentService() {
        this.em = JPAUtil.getEntityManager();  // tạo EntityManager 1 lần
        this.enrollmentDAO = new EnrollmentDAO(em); // tạo DAO 1 lần
    }

    // Tạo mới Enrollment
    public void createEnrollment(Enrollment enrollment) {
        enrollmentDAO.create(enrollment);
    }

    // Tìm Enrollment theo ID
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentDAO.findById(id);
    }

    // Cập nhật Enrollment
    public void updateEnrollment(Enrollment enrollment) {
        enrollmentDAO.update(enrollment);
    }

    // Xóa Enrollment theo ID
    public void deleteEnrollment(Long id) {
        enrollmentDAO.delete(id);
    }

    // Lấy danh sách tất cả Enrollment
    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.findAll();
    }

    // Đóng EntityManager khi không dùng nữa
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
