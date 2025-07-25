package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.Category;
import org.example.rf.model.Course;
import org.example.rf.service.CategoryService;
import org.example.rf.service.CourseService;

import java.io.IOException;
import java.util.List;

@WebServlet("/category")
public class CategoryServlet extends HttpServlet {
    private final CourseService courseService = new CourseService();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Course> categoryList = courseService.getAllCourses(); 
        request.setAttribute("categoryList", categoryList);

        List<Course> featuredCourses = courseService.getTop4Courses();
        request.setAttribute("url", request.getRequestURL().toString());
        request.setAttribute("featuredCourses", featuredCourses);
        request.getRequestDispatcher("category.jsp").forward(request, response);

    }
}

