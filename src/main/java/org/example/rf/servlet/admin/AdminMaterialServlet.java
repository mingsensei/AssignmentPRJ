package org.example.rf.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.rf.model.Material;
import org.example.rf.service.MaterialService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@WebServlet("/admin/material/*")
public class AdminMaterialServlet extends HttpServlet {
    private final MaterialService materialService = new MaterialService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
        List<Material> materials = materialService.getAllMaterials();
        request.setAttribute("materials", materials);
        request.getRequestDispatcher("view/admin/material.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo.equals("/uploads")) {
            uploadMaterial(request, response);
            response.sendRedirect("/admin/material");
        }
    }

    private void uploadMaterial(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String title = request.getParameter("title");
            String chapterIdParam = request.getParameter("chapterId");
            String type = "PDF";
            Part filePart = request.getPart("pdfFile");
            Long chapterId = Long.parseLong(chapterIdParam);

            System.out.println("Bắt đầu quá trình upload cho chapter ID: " + chapterId);

            materialService.uploadMaterialToDrive(title, chapterId, type, filePart);

            System.out.println("Upload và xử lý thành công!");

        } catch (NumberFormatException e) {
            System.err.println("Lỗi: Chapter ID không hợp lệ.");
            e.printStackTrace();
            throw new ServletException("Chapter ID không hợp lệ.", e);
        } catch (GeneralSecurityException e) {
            System.err.println("Lỗi bảo mật với Google Drive API.");
            e.printStackTrace();
            throw new ServletException("Lỗi khi kết nối tới Google Drive, vui lòng kiểm tra lại credentials.", e);
        } catch (Exception e) {
            System.err.println("Đã có lỗi không mong muốn xảy ra trong quá trình upload.");
            e.printStackTrace();
            throw new ServletException("Lỗi trong quá trình upload file.", e);
        }
    }
}
