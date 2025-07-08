package org.example.rf.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.Blog;
import org.example.rf.model.BlogUser;
import org.example.rf.model.User;
import org.example.rf.service.BlogService;
import org.example.rf.service.BlogUserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "BlogServlet", urlPatterns = {"/blog"})
public class BlogServlet extends HttpServlet {

    private BlogService blogService;
    private BlogUserService blogUserService;

    @Override
    public void init() {
        blogService = new BlogService();
        blogUserService = new BlogUserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "create":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    showBlog(request, response);
                    break;
            }
        } catch (ServletException | IOException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "create":
                    createBlog(request, response);
                    break;
                case "update":
                    updateBlog(request, response);
                    break;
                default:
                    response.sendRedirect("blog");
                    break;
            }
        } catch (ServletException | IOException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "BlogServlet handles viewing, creating, and editing blog posts.";
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("action", "create");
        RequestDispatcher dispatcher = request.getRequestDispatcher("editorBlog.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.valueOf(request.getParameter("id"));
        User user = (User) request.getSession().getAttribute("user");
        Blog blog = blogService.getBlogById(id);

        if (blog != null && user != null) {
            request.setAttribute("action", "update");
            request.setAttribute("blog", blog);
            RequestDispatcher dispatcher = request.getRequestDispatcher("editorBlog.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("blog?id=" + id + "&error=Unauthorized");
        }
    }

    private void showBlog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                Long id = Long.valueOf(idParam);
                Blog blog = blogService.getBlogById(id);

                if (blog != null) {
                    List<BlogUser> blogUsers = blogUserService.findByBlogId(id);
                    request.setAttribute("blogUsers", blogUsers);
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

    private void createBlog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (title == null || content == null) {
            response.sendRedirect("editorBlog.jsp?error=Missing+fields");
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

    private void updateBlog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        Long id = Long.valueOf(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Blog blog = blogService.getBlogById(id);

        if (user != null && blog != null && !blogUserService.findByBlogUser(user.getId(), blog.getId()).isEmpty()) {
            blog.setTitle(title);
            blog.setContent(content);
            blog.setUpdatedAt(LocalDateTime.now());
            blogService.updateBlog(blog);
            response.sendRedirect("blog?id=" + id);
        } else {
            response.sendRedirect("blog?id=" + id + "&error=Unauthorized");
        }
    }
}
