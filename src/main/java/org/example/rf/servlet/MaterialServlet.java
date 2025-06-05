package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.rf.model.Chapter;
import org.example.rf.model.Course;
import org.example.rf.model.Material;
import org.example.rf.service.ChapterService;
import org.example.rf.service.CourseService;
import org.example.rf.service.MaterialService;

import java.io.IOException;
import java.util.List;

@WebServlet("/material/*")
public class MaterialServlet extends HttpServlet {
    private final MaterialService materialService = new MaterialService();
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect("home.jsp");
        } else if (pathInfo.startsWith("/uploads")) {
            uploadMaterial(request, response);
            response.sendRedirect("home.jsp");
        }
    }

    private void uploadMaterial(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String chapterIdParam = request.getParameter("chapterId");
        System.out.println(">>> chapterIdParam = " + chapterIdParam);
        Long chapterId = Long.parseLong(chapterIdParam);
        String type = "PDF";
        Part filePart = request.getPart("pdfFile");
        String applicationPath = request.getServletContext().getRealPath("");
        Material material = materialService.uploadMaterials(title, chapterId, type, filePart, applicationPath);
    }

    private String getSubmittedFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                return item.substring(item.indexOf("=") + 2, item.length() - 1);
            }
        }
        return "";
    }
}
