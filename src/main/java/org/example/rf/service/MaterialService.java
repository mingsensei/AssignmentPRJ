package org.example.rf.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Part;
import org.example.rf.api.python.PythonApiClient;
import org.example.rf.dao.MaterialDAO;
import org.example.rf.model.Material;
import org.example.rf.util.DriveServiceUtil;
import org.example.rf.util.JPAUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.List;

public class MaterialService {
    private final MaterialDAO materialDAO;
    private final PythonApiClient pythonApiClient;

    public MaterialService() {
        EntityManager em = JPAUtil.getEntityManager();
        this.materialDAO = new MaterialDAO(em);
        this.pythonApiClient = new PythonApiClient();
    }


    public void uploadMaterialToDrive(String title, Long chapterId, String type, Part filePart) throws IOException, GeneralSecurityException {
        Drive driveService = DriveServiceUtil.getDriveService();
        String originalFileName = getSubmittedFileName(filePart);
        
        File uploadedFile = driveService.files().create(
                new File().setName(title + " - " + originalFileName),
                new com.google.api.client.http.InputStreamContent(filePart.getContentType(), filePart.getInputStream())
        ).setFields("id, webViewLink").execute();

        System.out.println("Upload lên Drive thành công! File ID: " + uploadedFile.getId());

        String vectorDbPath;
        java.io.File tempFile = null;
        try {
            try (InputStream fileContentFromDrive = driveService.files().get(uploadedFile.getId()).executeMediaAsInputStream()) {
                java.io.File tempDir = new java.io.File(System.getProperty("user.home"), ".reading_comprehension_temp");
                if (!tempDir.exists()) {
                    tempDir.mkdirs();
                }
                tempFile = java.io.File.createTempFile("gdrive-" + uploadedFile.getId() + "-", ".pdf", tempDir);
                Files.copy(fileContentFromDrive, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Đã lưu file tạm thời từ Google Drive tại: " + tempFile.getAbsolutePath());
            }

            vectorDbPath = pythonApiClient.callPythonAPIPdf(tempFile.getAbsolutePath(), chapterId.toString());

        } catch (Exception e) {
            System.err.println("Lỗi nghiêm trọng khi xử lý file phía backend (Python). Bắt đầu dọn dẹp...");
            e.printStackTrace();

            try {
                driveService.files().delete(uploadedFile.getId()).execute();
                System.out.println("Đã xóa file mồ côi trên Google Drive thành công. File ID: " + uploadedFile.getId());
            } catch (IOException deleteException) {
                System.err.println("Không thể xóa file trên Google Drive. Vui lòng xóa thủ công. File ID: " + uploadedFile.getId());
            }

            throw new IOException("Xử lý file phía backend thất bại, đã hủy bỏ thao tác.", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                if (tempFile.delete()) {
                    System.out.println("Đã xóa file tạm: " + tempFile.getName());
                } else {
                    System.err.println("Không thể xóa file tạm: " + tempFile.getName());
                }
            }
        }

        System.out.println("Xử lý backend thành công. Bắt đầu lưu vào database...");
        Material material = Material.builder()
                .chapterId(chapterId)
                .link(uploadedFile.getWebViewLink())
                .vectorDbPath(vectorDbPath)
                .title(title)
                .type(type)
                .build();
        materialDAO.create(material);
    }

    private String getSubmittedFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "unknown_file";
    }

    public List<Material> getAllMaterials() {
        return materialDAO.findAll();
    }
}
