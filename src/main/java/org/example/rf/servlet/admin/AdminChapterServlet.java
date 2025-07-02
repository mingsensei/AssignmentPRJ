package org.example.rf.servlet.admin;

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

@WebServlet("/admin/chapter/*")
public class AdminChapterServlet extends HttpServlet {

    private final ChapterService chapterService = new ChapterService();
    private final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || path.equals("/")) {
            listChapters(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        switch (path) {
            case "/add" -> addNewChapter(request, response);
            case "/update" -> updateChapter(request, response);
            case "/delete" -> deleteChapter(request, response);
            default -> response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    private void listChapters(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        List<Chapter> chapters = chapterService.getChaptersByCourseId(courseId);
        Course course = courseService.getCourseById(courseId);
        String courseName = course.getName();
        request.setAttribute("chapters", chapters);
        request.setAttribute("courseName", courseName);
        request.setAttribute("courseId", courseId);
        request.getRequestDispatcher("/view/admin/chapter.jsp").forward(request, response);
    }

    private void addNewChapter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        String title = request.getParameter("title");
        Integer orderIndex = Integer.parseInt(request.getParameter("orderIndex"));

        Chapter chapter = Chapter.builder()
                .courseId(courseId)
                .title(title)
                .orderIndex(orderIndex)
                .build();

        chapterService.createChapter(chapter);
        response.sendRedirect(request.getContextPath() + "/admin/chapter?courseId=" + courseId);
    }

    private void updateChapter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long chapterId = Long.parseLong(request.getParameter("chapterId"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        String title = request.getParameter("title");
        Integer orderIndex = Integer.parseInt(request.getParameter("orderIndex"));

        Chapter chapter = Chapter.builder()
                .id(chapterId)
                .courseId(courseId)
                .title(title)
                .orderIndex(orderIndex)
                .build();

        chapterService.updateChapter(chapter);
        response.sendRedirect(request.getContextPath() + "/admin/chapter?courseId=" + courseId);
    }

    private void deleteChapter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long chapterId = Long.parseLong(request.getParameter("chapterId"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));

        chapterService.deleteChapter(chapterId);
        response.sendRedirect(request.getContextPath() + "/admin/chapter?courseId=" + courseId);
    }
}