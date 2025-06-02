package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.Category;
import org.example.rf.service.CategoryService;

import java.io.IOException;
import java.util.List;

@WebServlet("/courses")
public class CourseServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryService categoryService = new CategoryService();
        List<Category> categoryList = categoryService.getAllCategories(); // Lấy dữ liệu từ DB
        request.setAttribute("categoryList", categoryList);

        List<Category> featuredCourses = categoryService.getTop4Courses(); // Lấy 4 khoá học đầu tiên
        request.setAttribute("featuredCourses", featuredCourses);
        request.getRequestDispatcher("/courses.jsp").forward(request, response);

    }
}

