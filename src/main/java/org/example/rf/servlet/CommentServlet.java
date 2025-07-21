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
import org.example.rf.model.CommentChapter;
import org.example.rf.model.CommentLesson;
import org.example.rf.service.BlogService;
import org.example.rf.service.ChapterService;
import org.example.rf.service.CommentService;
import org.example.rf.service.CommentBlogService;
import org.example.rf.service.CommentChapterService;
import org.example.rf.service.CommentCourseService;
import org.example.rf.service.CommentLessonService;
import org.example.rf.service.CourseService;
import org.example.rf.service.LessonService;

@WebServlet(name = "CommentServlet", urlPatterns = {"/Comment"})
public class CommentServlet extends HttpServlet {

    private CommentService commentService;
    private CommentBlogService commentBlogService;
    private CommentCourseService commentCourseService;
    private CommentChapterService commentChapterService;
    private CommentLessonService commentLessonService;
    private BlogService blogService;
    private CourseService courseService;
    private ChapterService chapterService;
    private LessonService lessonService;

    @Override
    public void init() throws ServletException {
        commentBlogService = new CommentBlogService();
        commentCourseService = new CommentCourseService();
        commentChapterService = new CommentChapterService();
        commentLessonService = new CommentLessonService();
        commentService = new CommentService();
        blogService = new BlogService();
        courseService = new CourseService();
        chapterService = new ChapterService();
        lessonService = new LessonService();
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

                switch (type) {
                    case "blog":
                        CommentBlog cb = new CommentBlog();
                        cb.setBlog(blogService.getBlogById(contentId));
                        cb.setComment(comment);
                        commentBlogService.create(cb);
                        break;
                    case "course":
                        CommentCourse cc = new CommentCourse();
                        cc.setCourse(courseService.getCourseById(contentId));
                        cc.setComment(comment);
                        commentCourseService.create(cc);
                        break;
                    case "chapter":
                        CommentChapter ch = new CommentChapter();
                        ch.setChapter(chapterService.getChapterById(contentId));
                        ch.setComment(comment);
                        commentChapterService.create(ch);
                        break;
                    case "lesson":
                        CommentLesson cl = new CommentLesson();
                        cl.setLesson(lessonService.getLessonById(contentId));
                        cl.setComment(comment);
                        commentLessonService.create(cl);
                        break;
                }

                if ("blog".equalsIgnoreCase(type)) {

                } else if ("course".equalsIgnoreCase(type)) {

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

        switch (type) {
            case "blog":
                comments = commentBlogService.findCommentsByBlogId(contentId);
            break;
            case "course":
                comments = commentCourseService.findCommentsByCourseId(contentId);
            break;
            case "chapter":
                comments = commentChapterService.findCommentsByChapterId(contentId);
            break;
            case "lesson":
                comments = commentLessonService.findCommentsByLessonId(contentId);
            break;
        }
        req.setAttribute("comments", comments);
        req.setAttribute("type", type);
        req.setAttribute("id", contentId);

        // Forward to a reusable JSP fragment
        req.getRequestDispatcher("commentSection.jsp").include(req, resp);
    }

}
