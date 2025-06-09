/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package org.example.rf.servlet;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.example.rf.model.Blog;
import org.example.rf.model.BlogUser;
import org.example.rf.service.BlogService;
import org.example.rf.service.BlogUserService;
import org.example.rf.service.UserService;

/**
 *
 * @author hongq
 */
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
        String idParam = request.getParameter("id");

        if (idParam != null) {
            // Show blog detail
            try {
                Long id = Long.parseLong(idParam);
                Blog blog = blogService.getBlogById(id);

                if (blog != null) {
                    List<BlogUser> blogusers = blogUserService.findByBlogId(id);
                    request.setAttribute("blogUsers", blogusers);
                    request.setAttribute("blog", blog);
                    for (BlogUser bu:blogusers) System.out.println(bu);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("blog.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Blog not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid blog ID");
            }
        } else {
            // Show all blogs
            List<Blog> blogs = blogService.getAllBlogs();
            request.setAttribute("blogs", blogs);
            RequestDispatcher dispatcher = request.getRequestDispatcher("blogs.jsp");
            dispatcher.forward(request, response);
        }
    }
}
