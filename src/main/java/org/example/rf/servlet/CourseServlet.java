package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.Chapter;
import org.example.rf.model.Course;
import org.example.rf.model.Enrollment;
import org.example.rf.model.User;
import org.example.rf.service.ChapterService;
import org.example.rf.service.CourseService;
import org.example.rf.service.EnrollmentService;
import org.example.rf.service.LessonService;
import org.example.rf.model.Lesson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/course")
public class CourseServlet extends HttpServlet {

    private ChapterService chapterService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;

    @Override
    public void init() {
        chapterService = new ChapterService();
        courseService = new CourseService();
        enrollmentService = new EnrollmentService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String courseIdParam = req.getParameter("courseId");
        try{
            User user = (User) req.getSession().getAttribute("user");
            if (courseIdParam == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing courseId");
                return;
            }

            Long courseId;
            try {
                courseId = Long.parseLong(courseIdParam);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid courseId");
                return;
            }

            Course course = courseService.getCourseById(courseId);
            if (course == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Course not found");
                return;
            }

            List<Chapter> chapters = chapterService.getChaptersByCourseId(courseId);

            // Lấy chapter đầu tiên và lesson đầu tiên (nếu có)
            Long firstChapterId = null;
            Long firstLessonId = null;
            if (chapters != null && !chapters.isEmpty()) {
                firstChapterId = chapters.get(0).getId();
                LessonService lessonService = new LessonService();
                List<Lesson> lessons = lessonService.getLessonsByChapterId(firstChapterId);
                if (lessons != null && !lessons.isEmpty()) {
                    firstLessonId = lessons.get(0).getId();
                }
                lessonService.close();
            }

            List<Enrollment> enrollments = enrollmentService.findByUserId(user.getId());
            List<Long> enrollmentIds = new ArrayList<>();
            for(Enrollment enrollment : enrollments) {
                enrollmentIds.add(enrollment.getCourse().getId());
            }

            req.setAttribute("course", course);
            req.setAttribute("chapters", chapters);
            req.setAttribute("enrollments", enrollmentIds);
            req.setAttribute("firstChapterId", firstChapterId);
            req.setAttribute("firstLessonId", firstLessonId);

        }catch (Exception e){
            Long courseId;
            courseId = Long.parseLong(courseIdParam);
            Course course = courseService.getCourseById(courseId);
            if (course == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Course not found");
                return;
            }
            req.setAttribute("course", course);
            List<Chapter> chapters = chapterService.getChaptersByCourseId(courseId);
            req.setAttribute("chapters", chapters);
        }

        req.getRequestDispatcher("/course.jsp").forward(req, resp);
    }
}
