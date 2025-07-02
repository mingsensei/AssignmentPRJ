package org.example.rf.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.Chapter;
import org.example.rf.model.Lesson;
import org.example.rf.service.ChapterService;
import org.example.rf.service.LessonService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/lesson/*")
public class AdminLessonServlet extends HttpServlet {
    private final LessonService lessonService = new LessonService();
    private final ChapterService chapterService = new ChapterService();
    private void listLessons(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] parts = request.getPathInfo().split("/");
        Long courseId = Long.parseLong(parts[1]);
        Long chapterId = Long.parseLong(parts[3]);

        List<Lesson> lessons = lessonService.getLessonsByChapterId(chapterId);
        Chapter chapter = chapterService.getChapterById(chapterId);

        request.setAttribute("lessons", lessons);
        request.setAttribute("chapter", chapter);
        request.setAttribute("courseId", courseId);
        request.getRequestDispatcher("/view/admin/lesson-list.jsp").forward(request, response);
    }
}
