package org.example.rf.service;

import org.example.rf.dao.CourseDAO;
import org.example.rf.model.Category;
import org.example.rf.model.Course;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class CourseService {

    private final CourseDAO courseDAO;

    public CourseService() {
        this.courseDAO = new CourseDAO();    // tạo DAO 1 lần dùng chung
    }

    // Tạo course mới
    public void createCourse(Course course) {
        courseDAO.create(course);
    }

    // Tìm course theo ID
    public Course getCourseById(Long id) {
        return courseDAO.findById(id);
    }

    // Cập nhật course
    public void updateCourse(Course course) {
        courseDAO.update(course);
    }

    // Xóa course theo ID
    public void deleteCourse(Long id) {
        courseDAO.delete(id);
    }

    // Lấy danh sách tất cả course
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    public ArrayList<Course> getTop4Courses() {
        return new ArrayList<>(courseDAO.getTop4Courses());
    }


}
