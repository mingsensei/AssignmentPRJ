package org.example.rf.service;

import jakarta.servlet.http.Part;
import org.example.rf.api.python.PythonApiClient;
import org.example.rf.dao.MaterialDAO;
import org.example.rf.model.Material;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MaterialService {
    private static final String UPLOAD_DIRECTORY = "uploads";
    private final EntityManager em;
    private final MaterialDAO materialDAO;
    private final PythonApiClient pythonApiClient;
    public MaterialService() {
        this.em = JPAUtil.getEntityManager();  // Tạo EntityManager 1 lần
        this.materialDAO = new MaterialDAO(em); // Tạo DAO 1 lần với EntityManager đó
        this.pythonApiClient = new PythonApiClient();
    }

    // Tạo mới Material
    public void createMaterial(Material material) {
        materialDAO.create(material);
    }

    // Tìm Material theo ID
    public Material getMaterialById(Long id) {
        return materialDAO.findById(id);
    }

    // Cập nhật Material
    public void updateMaterial(Material material) {
        materialDAO.update(material);
    }

    // Xóa Material theo ID
    public void deleteMaterial(Long id) {
        materialDAO.delete(id);
    }

    // Lấy danh sách tất cả Material
    public List<Material> getAllMaterials() {
        return materialDAO.findAll();
    }

    // Đóng EntityManager khi không còn sử dụng
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    public Material uploadMaterials(String title, Long chapterId, String type, Part filePart, String applicationPath) throws IOException {
        String fileName = getSubmittedFileName(filePart);
        // 4. Tạo thư mục uploads (nếu chưa tồn tại)
        String uploadPath = applicationPath + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 5. Tạo tên file duy nhất để tránh trùng lặp
        String uniqueFileName = UUID.randomUUID().toString() + "-" + fileName;
        String filePath = uploadPath + File.separator + uniqueFileName;  // Đường dẫn PDF trên server JAVA

        // 6. Lưu file lên server JAVA
        filePart.write(filePath);

        // 7. Gọi FastAPI để xử lý PDF và tạo VectorDB
        String vectorDbPath = pythonApiClient.callPythonAPIPdf(filePath, chapterId.toString());

        // 8. Tạo đối tượng Material
        Material material = Material.builder()
                .chapterId(chapterId)
                .link(filePath)
                .vectorDbPath(vectorDbPath)
                .title(title)
                .type(type)
                .build();

        // 9. Lưu thông tin vào database (bao gồm cả đường dẫn PDF VÀ đường dẫn VectorDB)
        materialDAO.create(material);
        return material;
    }

    private String getSubmittedFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                return item.substring(item.indexOf("=") + 2, item.length() - 1);
            }
        }
        return "";
    }
}
