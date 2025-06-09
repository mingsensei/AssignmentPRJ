package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.rf.dao.EnrollmentDAO;
import org.example.rf.model.Chapter;
import org.example.rf.model.Course;
import org.example.rf.model.Enrollment;
import org.example.rf.model.User;
import org.example.rf.service.ChapterService;
import org.example.rf.service.CourseService;
import org.example.rf.service.EnrollmentService;
import org.hibernate.Session;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/enrollment")
public class EnrollmentServlet extends HttpServlet {

    private EnrollmentService enSer = new EnrollmentService();
    private CourseService courseSer = new CourseService();
    private ChapterService chapterService = new ChapterService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = new User();
        if (session != null) {
             user = (User) session.getAttribute("user"); // "user" là key bạn lưu user lúc đăng nhập
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
            }
        }
        Long userId = user.getId(); // Bạn có thể lấy từ session hoặc request param
        if(userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        }
        ArrayList<Enrollment> enrollments = enSer.findByUserId(userId);

        // Lấy danh sách các course_id
        List<Long> courseIds = enrollments.stream()
                .map(e -> e.getCourse().getId()) // hoặc e.getCourseId() nếu không dùng @ManyToOne
                .collect(Collectors.toList());
        ArrayList<Course> courseList  = new ArrayList<>();
        List<Integer> chapters = new ArrayList<>();
        for (Long courseId : courseIds) {
            courseList.add(courseSer.getCourseById(courseId));
            chapters.add(chapterService.getChaptersByCourseId(courseId).size());
        }

        request.setAttribute("chapters", chapters);
        request.setAttribute("courseList", courseList);
        request.getRequestDispatcher("enrollment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // xử lý giống GET
    }
}
