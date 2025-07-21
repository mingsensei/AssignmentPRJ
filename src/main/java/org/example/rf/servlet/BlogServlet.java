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
        if (action == null) action = "";

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
        if (action == null) action = "";

        try {
            switch (action) {
                case "create":
                    createBlog(request, response);
                    break;
                case "update":
                    updateBlog(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/blog");
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
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/blog?error=Missing+blog+ID");
            return;
        }

        Long id;
        try {
            id = Long.valueOf(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/blog?error=Invalid+blog+ID");
            return;
        }

        Blog blog = blogService.getBlogById(id);
        if (blog == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Blog not found");
            return;
        }

        // Check if the user is authorized (is an author/editor of the blog)
        List<BlogUser> roles = blogUserService.findByBlogUser(user.getId(), blog.getId());
        if (roles.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/blog?id=" + id + "&error=Unauthorized");
            return;
        }

        request.setAttribute("action", "update");
        request.setAttribute("blog", blog);
        RequestDispatcher dispatcher = request.getRequestDispatcher("editorBlog.jsp");
        dispatcher.forward(request, response);
    }

    private void showBlog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam != null) {
            Long id;
            try {
                id = Long.valueOf(idParam);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid blog ID");
                return;
            }

            Blog blog = blogService.getBlogById(id);
            if (blog == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Blog not found");
                return;
            }

            List<BlogUser> blogUsers = blogUserService.findByBlogId(id);
            request.setAttribute("blogUsers", blogUsers);
            request.setAttribute("blog", blog);
            blogService.incrementViewCount(id);

            RequestDispatcher dispatcher = request.getRequestDispatcher("blog.jsp");
            dispatcher.forward(request, response);
        } else {
            // Show blog listing page
            List<Blog> newestBlogs = blogService.getTopNewestBlogs();
            List<Blog> mostViewedBlogs = blogService.getTopViewedBlogs(5);
            request.setAttribute("newestBlogs", newestBlogs);
            request.setAttribute("mostViewed", mostViewedBlogs);

            RequestDispatcher dispatcher = request.getRequestDispatcher("blogs.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void createBlog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String description = request.getParameter("description");
        String thumbnail = request.getParameter("thumbnail");

        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/blog?action=create&error=Missing+title+or+content");
            return;
        }

        Blog blog = new Blog();
        blog.setTitle(title.trim());
        blog.setContent(content.trim());
        blog.setDescription(description != null ? description.trim() : null);
        blog.setThumbnail(thumbnail != null ? thumbnail.trim() : null);
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());

        blogService.createBlog(blog);

        BlogUser blogUser = new BlogUser();
        blogUser.setAssignedAt(LocalDateTime.now());
        blogUser.setBlog(blog);
        blogUser.setBlogRole("author");
        blogUser.setUser(user);

        blogUserService.createBlogUser(blogUser);

        response.sendRedirect(request.getContextPath() + "/blog?id=" + blog.getId());
    }

    private void updateBlog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/blog?error=Missing+blog+ID");
            return;
        }

        Long id;
        try {
            id = Long.valueOf(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/blog?error=Invalid+blog+ID");
            return;
        }

        Blog blog = blogService.getBlogById(id);
        if (blog == null) {
            response.sendRedirect(request.getContextPath() + "/blog?error=Blog+not+found");
            return;
        }

        // Check if user is authorized (must be associated with blog)
        List<BlogUser> roles = blogUserService.findByBlogUser(user.getId(), blog.getId());
        if (roles.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/blog?id=" + id + "&error=Unauthorized");
            return;
        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String description = request.getParameter("description");
        String thumbnail = request.getParameter("thumbnail");

        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/blog?action=edit&id=" + id + "&error=Missing+title+or+content");
            return;
        }

        blog.setTitle(title.trim());
        blog.setContent(content.trim());
        blog.setDescription(description != null ? description.trim() : null);
        blog.setThumbnail(thumbnail != null ? thumbnail.trim() : null);
        blog.setUpdatedAt(LocalDateTime.now());

        blogService.updateBlog(blog);

        response.sendRedirect(request.getContextPath() + "/blog?id=" + id);
    }
}
