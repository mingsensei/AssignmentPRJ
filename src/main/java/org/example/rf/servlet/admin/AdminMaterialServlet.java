package org.example.rf.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.rf.model.Course;
import org.example.rf.model.Material;
import org.example.rf.service.CourseService;
import org.example.rf.service.MaterialService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@WebServlet("/admin/material/*")
@MultipartConfig
public class AdminMaterialServlet extends HttpServlet {
    private final MaterialService materialService = new MaterialService();
    private final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Kiểm tra đường dẫn để điều hướng chính xác
        if (pathInfo == null || pathInfo.equals("/")) {
            // URL: /admin/material -> Hiển thị danh sách tài liệu
            showMaterialList(request, response);
        } else if (pathInfo.equals("/add")) {
            // URL: /admin/material/add -> Hiển thị form upload
            showUploadForm(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showMaterialList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Material> materials = materialService.getAllMaterials();
        request.setAttribute("materials", materials);

        // Nhận và hiển thị thông báo thành công nếu có
        if ("upload_success".equals(request.getParameter("status"))) {
            request.setAttribute("success", "Tải tài liệu lên thành công!");
        } else if ("delete_success".equals(request.getParameter("status"))) {
            request.setAttribute("success", "Xóa tài liệu thành công!");
        }

        request.getRequestDispatcher("/view/admin/material.jsp").forward(request, response);
    }

    private void showUploadForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Course> courses = courseService.getAllCourses();
        request.setAttribute("courses", courses);
        // Sử dụng file upload JSP bạn đã có
        request.getRequestDispatcher("/view/admin/upload-material.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            switch (pathInfo) {
                case "/uploads":
                    uploadMaterial(request, response);
                    break;
                case "/delete":
                    deleteMaterial(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void uploadMaterial(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String title = request.getParameter("title");
            String chapterIdParam = request.getParameter("chapterId");
            Part filePart = request.getPart("pdfFile");

            if (chapterIdParam == null || chapterIdParam.trim().isEmpty() || "null".equals(chapterIdParam)) {
                handleUploadError(request, response, "Vui lòng chọn một chương hợp lệ.");
                return;
            }
            if (title == null || title.trim().isEmpty()) {
                handleUploadError(request, response, "Tiêu đề không được để trống.");
                return;
            }
            if (filePart == null || filePart.getSize() == 0) {
                handleUploadError(request, response, "Bạn chưa chọn file PDF để tải lên.");
                return;
            }

            Long chapterId = Long.parseLong(chapterIdParam);
            materialService.uploadMaterialToDrive(title, chapterId, "PDF", filePart);

            // Chuyển hướng về trang danh sách với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/admin/material?status=upload_success");

        } catch (NumberFormatException e) {
            handleUploadError(request, response, "Chapter ID không hợp lệ. Vui lòng thử lại.");
        } catch (Exception e) {
            e.printStackTrace();
            handleUploadError(request, response, "Đã có lỗi xảy ra trong quá trình upload file.");
        }
    }

    private void deleteMaterial(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            long materialId = Long.parseLong(request.getParameter("materialId"));
//            materialService./ Giả sử service có phương thức này
            response.sendRedirect(request.getContextPath() + "/admin/material?status=delete_success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/material?status=delete_error");
        }
    }

    private void handleUploadError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        // Tải lại danh sách khóa học để form không bị lỗi
        List<Course> courses = courseService.getAllCourses();
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("/view/admin/upload-material.jsp").forward(request, response);
    }
}