package org.example.rf.service;

import org.example.rf.dao.EnrollmentDAO;
import org.example.rf.model.Course;
import org.example.rf.model.Enrollment;
import org.example.rf.model.Order;
import org.example.rf.model.User;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentService {
    private final EnrollmentDAO enrollmentDAO;

    public EnrollmentService() {
        this.enrollmentDAO = new EnrollmentDAO(); // tạo DAO 1 lần
    }

    // Tạo mới Enrollment
    public void createEnrollment(User user, List<Course> courses) {
        for (Course course : courses) {
            Enrollment enrollment = Enrollment.builder()
                    .user(user)
                    .course(course)
                    .enrolledAt(LocalDateTime.now())
                    .build();
            enrollmentDAO.create(enrollment);
        }
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


    public ArrayList<Enrollment> findByUserId(long userId){
        return new ArrayList<>(enrollmentDAO.findByUserId(userId));
    }

}
