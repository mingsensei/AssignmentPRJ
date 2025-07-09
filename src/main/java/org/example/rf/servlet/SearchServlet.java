package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.Course;
import org.example.rf.service.CourseService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private CourseService courseService;

    @Override
    public void init() {
        courseService = new CourseService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        List<Course> courses = new ArrayList<>();
        if (query != null && !query.trim().isEmpty()) {
            courses = courseService.searchCourses(query.trim());
        }
        request.setAttribute("courses", courses);
        request.setAttribute("query", query);
        request.getRequestDispatcher("searchresult.jsp").forward(request, response);
    }

    @Override
    public void destroy() {
        courseService.close();
        super.destroy();
    }
}
