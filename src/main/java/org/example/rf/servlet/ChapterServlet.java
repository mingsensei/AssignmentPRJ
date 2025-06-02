package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.Chapter;
import org.example.rf.model.Course;
import org.example.rf.service.ChapterService;
import org.example.rf.service.CourseService;

import java.io.IOException;
import java.util.List;

@WebServlet("/chapter")
public class ChapterServlet extends HttpServlet {

    private ChapterService chapterService;

    @Override
    public void init() {
        chapterService = new ChapterService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if(path.equals("/chapter")) {
            int chapterId = Integer.parseInt(request.getParameter("chapterId"));
            request.setAttribute("chapterId", chapterId);
            request.getRequestDispatcher("/chapter.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        chapterService.close();
        super.destroy();
    }
}