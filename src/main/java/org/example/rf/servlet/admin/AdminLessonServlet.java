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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listLessons(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (action) {
            case "/add":
                addLesson(request, response);
                break;
            case "/update":
                updateLesson(request, response);
                break;
            case "/delete":
                deleteLesson(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void listLessons(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String chapterIdStr = request.getParameter("chapterId");
        if (chapterIdStr == null || chapterIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Chapter ID không được cung cấp.");
            return;
        }
        try {
            long chapterId = Long.parseLong(chapterIdStr);
            Chapter chapter = chapterService.getChapterById(chapterId);
            if (chapter == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy chương học.");
                return;
            }
            List<Lesson> lessons = lessonService.getLessonsByChapterId(chapterId);

            request.setAttribute("lessons", lessons);
            request.setAttribute("chapterId", chapter.getId());
            request.setAttribute("chapterTitle", chapter.getTitle());
            request.setAttribute("courseId", chapter.getCourseId());

            request.getRequestDispatcher("/view/admin/lesson.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Chapter ID không hợp lệ.");
        }
    }

    private void addLesson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long chapterId = Long.parseLong(request.getParameter("chapterId"));

        // Lấy Chapter để có courseId, vì Lesson model yêu cầu
        Chapter chapter = chapterService.getChapterById(chapterId);
        if (chapter == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy chương học để thêm bài học.");
            return;
        }

        Lesson lesson = new Lesson();
        lesson.setTitle(request.getParameter("title"));
        lesson.setOrderIndex(Integer.parseInt(request.getParameter("orderIndex")));
        lesson.setDescription(request.getParameter("description"));
        lesson.setVideoUrl(request.getParameter("videoUrl"));
        lesson.setChapterId(chapterId);
        lesson.setCourseId(chapter.getCourseId()); // Gán courseId từ chapter

        lessonService.createLesson(lesson);
        response.sendRedirect(request.getContextPath() + "/admin/lesson?chapterId=" + chapterId);
    }

    private void updateLesson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long lessonId = Long.parseLong(request.getParameter("lessonId"));
        long chapterId = Long.parseLong(request.getParameter("chapterId"));

        // Lấy thông tin bài học hiện tại để có courseId
        Lesson existingLesson = lessonService.getLessonById(lessonId);
        if (existingLesson == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy bài học để cập nhật.");
            return;
        }

        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setTitle(request.getParameter("title"));
        lesson.setOrderIndex(Integer.parseInt(request.getParameter("orderIndex")));
        lesson.setDescription(request.getParameter("description"));
        lesson.setVideoUrl(request.getParameter("videoUrl"));
        lesson.setChapterId(chapterId);
        lesson.setCourseId(existingLesson.getCourseId()); // Giữ nguyên courseId cũ

        lessonService.updateLesson(lesson);
        response.sendRedirect(request.getContextPath() + "/admin/lesson?chapterId=" + chapterId);
    }

    private void deleteLesson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long lessonId = Long.parseLong(request.getParameter("lessonId"));
        long chapterId = Long.parseLong(request.getParameter("chapterId"));

        lessonService.deleteLesson(lessonId);
        response.sendRedirect(request.getContextPath() + "/admin/lesson?chapterId=" + chapterId);
    }
}