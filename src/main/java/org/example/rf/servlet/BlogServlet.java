package org.example.rf.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.Blog;
import org.example.rf.model.BlogUser;
import org.example.rf.service.BlogService;
import org.example.rf.service.BlogUserService;
import org.example.rf.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.example.rf.model.User;

@WebServlet(name = "BlogServlet", urlPatterns = {"/blog"})
public class BlogServlet extends HttpServlet {

    private BlogService blogService;
    private BlogUserService blogUserService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        blogService = new BlogService();
        blogUserService = new BlogUserService();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action==null || "".equals(action)) action="views";
        if ("create".equalsIgnoreCase(action)) {
            // 👉 Show blog creation page
            RequestDispatcher dispatcher = request.getRequestDispatcher("createBlog.jsp");
            dispatcher.forward(request, response);
            return;
        }

        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                Long id = Long.parseLong(idParam);
                Blog blog = blogService.getBlogById(id);

                if (blog != null) {
                    List<BlogUser> blogusers = blogUserService.findByBlogId(id);
                    request.setAttribute("blogUsers", blogusers);
                    request.setAttribute("blog", blog);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("blog.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Blog not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid blog ID");
            }
        } else {
            List<Blog> blogs = blogService.getAllBlogs();
            request.setAttribute("blogs", blogs);
            RequestDispatcher dispatcher = request.getRequestDispatcher("blogs.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action==null || "".equals(action)) action="views";
        if (!"create".equalsIgnoreCase(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported action.");
            return;
        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        User user = (User) request.getSession().getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        if (title == null || content == null) {
            response.sendRedirect("createBlog.jsp?error=Missing+fields");
            return;
        }

        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());
        
        BlogUser bloguser = new BlogUser();
        bloguser.setAssignedAt(LocalDateTime.now());
        bloguser.setBlog(blog);
        bloguser.setBlogRole("author");
        bloguser.setUser(user);

        blogService.createBlog(blog);
        blogUserService.createBlogUser(bloguser);
        
        response.sendRedirect(request.getContextPath() + "/blog?id=" + blog.getId());
    }
}
