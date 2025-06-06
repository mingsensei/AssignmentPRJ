package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.Course;
import org.example.rf.model.Chapter;
import org.example.rf.service.CourseService;
import org.example.rf.service.ChapterService;

import java.io.IOException;
import java.util.List;

@WebServlet("/courses")
public class CourseServlet extends HttpServlet {

    private CourseService courseService;
    private ChapterService chapterService;

    @Override
    public void init() {
        courseService = new CourseService();
        chapterService = new ChapterService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String courseIdParam = req.getParameter("courseId");

        if (courseIdParam == null) {
            List<Course> courseList = courseService.getAllCourses();
            req.setAttribute("courseList", courseList);

            List<Course> featuredCourses = courseService.getTop4Courses(); // Lấy 4 khoá học đầu tiên
            req.setAttribute("featuredCourses", featuredCourses);

            req.getRequestDispatcher("/courses.jsp").forward(req, resp);
        } else {
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

            req.setAttribute("course", course);
            req.setAttribute("chapters", chapters);

            req.getRequestDispatcher("/courseDetails.jsp").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        chapterService.close();
        courseService.close();
        super.destroy();
    }
}