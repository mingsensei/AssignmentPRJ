package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.rf.model.Comment;
import org.example.rf.model.CommentBlog;
import org.example.rf.model.CommentCourse;
import org.example.rf.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.example.rf.service.BlogService;
import org.example.rf.service.CommentService;
import org.example.rf.service.CommentBlogService;
import org.example.rf.service.CommentCourseService;
import org.example.rf.service.CourseService;

@WebServlet(name = "CommentServlet", urlPatterns = {"/Comment"})
public class CommentServlet extends HttpServlet {

    private CommentService commentService;
    private CommentBlogService commentBlogService;
    private CommentCourseService commentCourseService;
    private BlogService blogService;
    private CourseService courseService;

    @Override
    public void init() throws ServletException {
        commentBlogService = new CommentBlogService();
        commentCourseService = new CommentCourseService();
        commentService = new CommentService();
        blogService = new BlogService();
        courseService = new CourseService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        String type = req.getParameter("type"); // "blog" or "course"
        Long contentId = Long.valueOf(req.getParameter("id"));
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("add".equals(action)) {
            String content = req.getParameter("content").trim();
            if (!content.isEmpty()) {
                Comment comment = new Comment();
                comment.setUser(user); // set full user, not just ID
                comment.setContent(content);
                comment.setCreatedAt(LocalDateTime.now());
                comment.setUpdatedAt(LocalDateTime.now());

                commentService.create(comment); // Save base comment

                if ("blog".equalsIgnoreCase(type)) {
                    CommentBlog cb = new CommentBlog();
                    cb.setBlog(blogService.getBlogById(contentId));
                    cb.setComment(comment);
                    commentBlogService.create(cb);
                } else if ("course".equalsIgnoreCase(type)) {
                    CommentCourse cc = new CommentCourse();
                    cc.setCourse(courseService.getCourseById(contentId));
                    cc.setComment(comment);
                    commentCourseService.create(cc);
                }
            }
            resp.sendRedirect(req.getHeader("Referer")); // Back to previous page
        }
    }

    // Optional: Load comments by type, if using this servlet for displaying comments too
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        Long contentId = Long.valueOf(req.getParameter("id"));

        List<Comment> comments = new ArrayList<>();

        if ("blog".equalsIgnoreCase(type)) {
            comments = commentBlogService.findCommentsByBlogId(contentId);
        } else if ("course".equalsIgnoreCase(type)) {
            comments = commentCourseService.findCommentsByCourseId(contentId);
        }

        req.setAttribute("comments", comments);
        req.setAttribute("type", type);
        req.setAttribute("id", contentId);

        // Forward to a reusable JSP fragment
        req.getRequestDispatcher("commentSection.jsp").include(req, resp);
    }

}
