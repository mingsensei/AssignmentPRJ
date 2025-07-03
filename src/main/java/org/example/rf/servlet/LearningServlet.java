package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.rf.model.*;
import org.example.rf.service.CourseService;
import org.example.rf.service.ChapterService;
import org.example.rf.service.LearningProgressService;
import org.example.rf.service.LessonService;
import org.json.JSONObject;

@WebServlet("/learning")
public class LearningServlet extends HttpServlet {
    LearningProgressService learningProgressService = new LearningProgressService();
    LessonService lessonService = new LessonService();
    Long courseIdd = 0L;
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
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
        }

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
        courseIdd = courseId;
        CompletionStatus status = learningProgressService.getCompletedStatusByCourse(user.getId(), courseId);
        request.setAttribute("completedLessonIds", status.getCompletedLessonIds());
        request.setAttribute("completedChapterIds", status.getCompletedChapterIds());
        request.setAttribute("isCompletedCourse", status.isCourseCompleted());

        request.setAttribute("course", course);
        request.setAttribute("chapters", chapters);
        request.setAttribute("lessons", lessons);
        request.setAttribute("allLessons", allLessons);
        request.setAttribute("lesson", lesson);
        assert user != null;


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


        // Đọc JSON từ request
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        User user = (User) request.getSession().getAttribute("user");

        try {
            JSONObject json = new JSONObject(sb.toString());
            Long userId = json.getLong("userId");
            Long lessonId = json.getLong("lessonId");

            Optional<LearningProgress> progressOpt = learningProgressService.getProgressByUserAndLesson(userId, lessonId);
            LearningProgress progress = progressOpt.orElseGet(() -> {
                LearningProgress lp = new LearningProgress();
                lp.setUser(user);
                lp.setLesson(lessonService.getLessonById(lessonId));
                lp.setCreatedAt(LocalDateTime.now());
                return lp;
            });

            progress.setIsCompleted(true);
            progress.setCompletedAt(LocalDateTime.now());

            if (progress.getId() == null) {
                learningProgressService.createProgress(progress);
            }else {
                learningProgressService.updateProgress(progress);
            }
            CompletionStatus status = learningProgressService.getCompletedStatusByCourse(user.getId(), courseIdd);
            request.setAttribute("completedLessonIds", status.getCompletedLessonIds());
            request.setAttribute("completedChapterIds", status.getCompletedChapterIds());
            request.setAttribute("isCompletedCourse", status.isCourseCompleted());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid data");
        }
    }
}

