package org.example.rf.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.Course;
import org.example.rf.service.CourseService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/course/*")
public class AdminCourseServlet extends HttpServlet {
    private final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            listCourses(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        switch (path) {
            case "/add" -> addNewCourse(request, response);
            case "/update" -> updateCourse(request, response);
            case "/delete" -> deleteCourse(request, response);
        }
    }

    private void listCourses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Course> courses = courseService.getAllCourses();
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("/view/admin/course.jsp").forward(request, response);
    }



    private void addNewCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        String type = request.getParameter("type");
        int semester = Integer.parseInt(request.getParameter("semester"));
        Long categoryId = Long.parseLong(request.getParameter("categoryId"));
        String description = request.getParameter("description");

        Course course = Course.builder()
                .price(price)
                .name(name)
                .type(type)
                .semester(semester)
                .description(description)
                .categoryId(categoryId)
                .build();

        courseService.createCourse(course);
        response.sendRedirect(request.getContextPath() + "/admin/course");
    }

    private void updateCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        String type = request.getParameter("type");
        Integer semester = Integer.parseInt(request.getParameter("semester"));
        Long categoryId = Long.parseLong(request.getParameter("categoryId"));
        String description = request.getParameter("description");

        Course course = Course.builder()
                .id(courseId)
                .name(name)
                .price(price)
                .type(type)
                .semester(semester)
                .categoryId(categoryId)
                .description(description)
                .build();

        courseService.updateCourse(course);
        response.sendRedirect(request.getContextPath() + "/admin/course");
    }

    private void deleteCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        courseService.deleteCourse(courseId);
        response.sendRedirect(request.getContextPath() + "/admin/course");
    }
}
