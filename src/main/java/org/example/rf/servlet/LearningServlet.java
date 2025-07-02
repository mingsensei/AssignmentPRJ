package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

import org.example.rf.model.Course;
import org.example.rf.model.Chapter;
import org.example.rf.model.Lesson;
import org.example.rf.service.CourseService;
import org.example.rf.service.ChapterService;
import org.example.rf.service.LessonService;

@WebServlet("/learning")
public class LearningServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long courseId = parseLong(request.getParameter("courseid"));
        Long chapterId = parseLong(request.getParameter("chapterid"));
        Long lessonId = parseLong(request.getParameter("lessonid"));

        CourseService courseService = new CourseService();
        ChapterService chapterService = new ChapterService();
        LessonService lessonService = new LessonService();

        Course course = null;
        if (courseId != null) {
            course = courseService.getCourseById(courseId);
        }
        List<Chapter> chapters = (courseId != null) ? chapterService.getChaptersByCourseId(courseId) : null;
        List<Lesson> lessons = (chapterId != null) ? lessonService.getLessonsByChapterId(chapterId) : null;
        Lesson lesson = (lessonId != null) ? lessonService.getLessonById(lessonId) : null;

        // Lấy toàn bộ lesson của tất cả các chương trong course
        List<Lesson> allLessons = null;
        if (chapters != null && !chapters.isEmpty()) {
            allLessons = new java.util.ArrayList<>();
            for (Chapter ch : chapters) {
                List<Lesson> ls = lessonService.getLessonsByChapterId(ch.getId());
                if (ls != null && !ls.isEmpty()) {
                    allLessons.addAll(ls);
                }
            }
        }

        request.setAttribute("course", course);
        request.setAttribute("chapters", chapters);
        request.setAttribute("lessons", lessons);
        request.setAttribute("allLessons", allLessons);
        request.setAttribute("lesson", lesson);

        request.getRequestDispatcher("/learning.jsp").forward(request, response);
    }

    private Long parseLong(String s) {
        try { return s == null ? null : Long.parseLong(s); } catch (Exception e) { return null; }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String comment = request.getParameter("comment");
        request.setAttribute("message", "Bình luận đã gửi: " + comment);
        request.getRequestDispatcher("/learning.jsp").forward(request, response);
    }
}

