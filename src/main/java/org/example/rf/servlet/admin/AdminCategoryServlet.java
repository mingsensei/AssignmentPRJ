package org.example.rf.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.Category;
import org.example.rf.service.CategoryService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/category/*")
public class AdminCategoryServlet extends HttpServlet {

    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || path.equals("/")) {
            listCategories(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();

        switch (path) {
            case "/add" -> addNewCategory(request, response);
            case "/update" -> updateCategory(request, response);
            case "/delete" -> deleteCategory(request, response);
            default -> response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Category> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/view/admin/settings.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải danh sách danh mục: " + e.getMessage());
            request.getRequestDispatcher("/view/admin/settings.jsp").forward(request, response);
        }
    }

    private void addNewCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Tên danh mục không được để trống");
                listCategories(request, response);
                return;
            }

            if (categoryService.categoryNameExists(name.trim(), null)) {
                request.setAttribute("errorMessage", "Tên danh mục đã tồn tại");
                listCategories(request, response);
                return;
            }

            Category category = Category.builder()
                    .name(name.trim())
                    .description(description != null ? description.trim() : "")
                    .build();

            categoryService.createCategory(category);
            request.setAttribute("successMessage", "Thêm danh mục thành công");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi thêm danh mục: " + e.getMessage());
        }

        listCategories(request, response);
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long categoryId = Long.parseLong(request.getParameter("categoryId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            // Validation
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Tên danh mục không được để trống");
                listCategories(request, response);
                return;
            }

            // Check if category exists
            Category existingCategory = categoryService.getCategoryById(categoryId);
            if (existingCategory == null) {
                request.setAttribute("errorMessage", "Không tìm thấy danh mục");
                listCategories(request, response);
                return;
            }

            // Check if category name already exists (exclude current category)
            if (categoryService.categoryNameExists(name.trim(), categoryId)) {
                request.setAttribute("errorMessage", "Tên danh mục đã tồn tại");
                listCategories(request, response);
                return;
            }

            Category category = Category.builder()
                    .id(categoryId)
                    .name(name.trim())
                    .description(description != null ? description.trim() : "")
                    .build();

            categoryService.updateCategory(category);
            request.setAttribute("successMessage", "Cập nhật danh mục thành công");

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID danh mục không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi cập nhật danh mục: " + e.getMessage());
        }

        listCategories(request, response);
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long categoryId = Long.parseLong(request.getParameter("categoryId"));

            // Check if category exists
            Category category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                request.setAttribute("errorMessage", "Không tìm thấy danh mục");
                listCategories(request, response);
                return;
            }

            // Check if category is being used by courses
            if (categoryService.isCategoryInUse(categoryId)) {
                request.setAttribute("errorMessage", "Không thể xóa danh mục đang được sử dụng bởi các khóa học");
                listCategories(request, response);
                return;
            }

            categoryService.deleteCategory(categoryId);
            request.setAttribute("successMessage", "Xóa danh mục thành công");

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID danh mục không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi xóa danh mục: " + e.getMessage());
        }

        listCategories(request, response);
    }
}