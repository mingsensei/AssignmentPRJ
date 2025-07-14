package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.rf.model.Chapter;
import org.example.rf.model.Course;
import org.example.rf.service.ChapterService;
import org.example.rf.service.CourseService;
import org.example.rf.service.MaterialService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@WebServlet("/material/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class MaterialServlet extends HttpServlet {
    private final CourseService courseService = new CourseService();
    private final ChapterService chapterService = new ChapterService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Course> courseList = courseService.getAllCourses();
            List<Chapter> chapterList = chapterService.getAllChapters();
            request.setAttribute("courses", courseList);
            request.setAttribute("chapters", chapterList);
            request.getRequestDispatcher("/upload-material.jsp").forward(request, response);
        }
    }


}
