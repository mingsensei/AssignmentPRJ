package org.example.rf.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import org.example.rf.model.Blog;
import org.example.rf.service.BlogService;

@WebServlet("/admin/blog/*")
public class AdminBlogServlet extends HttpServlet {

    private final BlogService blogService = new BlogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || path.equals("/")) {
            listBlogs(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        switch (path) {
            case "/delete" ->
                deleteBlog(request, response);
            default ->
                response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    private void listBlogs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Blog> blogs = blogService.getAllBlogs();
            request.setAttribute("blogs", blogs);
            request.getRequestDispatcher("/view/admin/blog.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải danh sách blog: " + e.getMessage());
            request.getRequestDispatcher("/view/admin/blog.jsp").forward(request, response);
        }
    }

    private void deleteBlog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long blogId = Long.parseLong(request.getParameter("blogId"));

            Blog blog = blogService.getBlogById(blogId);
            if (blog == null) {
                request.setAttribute("errorMessage", "Không tìm thấy blog");
                listBlogs(request, response);
                return;
            }

            blogService.deleteBlog(blogId);
            request.setAttribute("successMessage", "Xóa blog thành công");

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID blog không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi xóa blog: " + e.getMessage());
        }

        listBlogs(request, response);
    }
}
