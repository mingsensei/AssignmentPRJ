<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, org.example.rf.model.Course" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Upload PDF Material</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { max-width: 600px; margin: 40px auto; background: #fff; border: 1px solid #ddd; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        h1 { text-align: center; color: #333; }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 8px; font-weight: bold; color: #555; }
        input[type="text"], select { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        input[type="file"] { padding: 8px 0; }
        button { background-color: #4CAF50; color: white; padding: 12px 20px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; width: 100%; transition: background-color 0.3s; }
        button:hover { background-color: #45a049; }
        button:disabled { background-color: #cccccc; cursor: not-allowed; }
        .error { color: #d9534f; background-color: #f2dede; border: 1px solid #ebccd1; padding: 15px; border-radius: 4px; margin-bottom: 15px; }
        .success { color: #3c763d; background-color: #dff0d8; border: 1px solid #d6e9c6; padding: 15px; border-radius: 4px; margin-bottom: 15px; }
        .home-link { display: inline-block; padding: 10px 20px; background-color: #6c757d; color: white; text-decoration: none; border-radius: 6px; font-weight: bold; margin-top: 30px; }
    </style>
</head>
<body>
<div class="container">
    <h1>Upload PDF Material</h1>

    <%-- Hiển thị thông báo lỗi nếu có từ Servlet --%>
    <%
        String error = (String) request.getAttribute("error");
        if (error != null && !error.isEmpty()) {
    %>
    <p class="error"><%= error %></p>
    <%
        }
    %>

    <form action="<%= request.getContextPath() %>/admin/material/uploads" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="title">Tiêu đề:</label>
            <input type="text" id="title" name="title" required>
        </div>

        <div class="form-group">
            <label for="courseId">Môn học:</label>
            <select id="courseId" name="courseId" required>
                <option value="">-- Chọn môn học --</option>
                <%
                    List<Course> courses = (List<Course>) request.getAttribute("courses");
                    if (courses != null) {
                        for (Course course : courses) {
                %>
                <option value="<%= course.getId() %>"><%= course.getName() %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <div class="form-group">
            <label for="chapterId">Chương:</label>
            <select id="chapterId" name="chapterId" required>
                <option value="">-- Vui lòng chọn môn học trước --</option>
            </select>
        </div>

        <div class="form-group">
            <label for="pdfFile">Chọn file PDF:</label>
            <input type="file" id="pdfFile" name="pdfFile" accept=".pdf" required>
        </div>

        <div class="form-group">
            <%-- Thêm ID cho nút để dễ dàng thao tác bằng JavaScript --%>
            <button type="submit" id="submitBtn">Tải lên</button>
        </div>
    </form>
</div>

<div style="text-align: center;">
    <a href="${pageContext.request.contextPath}/admin/material" class="home-link">← Về trang chủ</a>
</div>


<script>
    document.addEventListener('DOMContentLoaded', function() {
        const courseSelect = document.getElementById('courseId');
        const chapterSelect = document.getElementById('chapterId');
        const submitButton = document.getElementById('submitBtn');

        // Hàm để cập nhật trạng thái nút submit
        function updateSubmitButtonState() {
            // Nút chỉ được bật khi cả môn học và chương đều đã được chọn
            const courseSelected = courseSelect.value !== "";
            const chapterSelected = chapterSelect.value !== "";
            submitButton.disabled = !(courseSelected && chapterSelected);
        }

        // Vô hiệu hóa nút submit ngay từ đầu
        updateSubmitButtonState();

        // Sự kiện khi thay đổi môn học
        courseSelect.addEventListener('change', function () {
            const courseId = this.value;

            // Xóa lựa chọn chương cũ và vô hiệu hóa nút
            chapterSelect.innerHTML = '<option value="">-- Chọn chương --</option>';
            updateSubmitButtonState();

            if (!courseId) {
                chapterSelect.innerHTML = '<option value="">-- Vui lòng chọn môn học trước --</option>';
                return;
            }

            // Hiển thị trạng thái đang tải
            chapterSelect.innerHTML = '<option value="">-- Đang tải chương... --</option>';

            fetch('<%=request.getContextPath()%>/chapters-by-course?courseId=' + encodeURIComponent(courseId))
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Lỗi mạng hoặc server');
                    }
                    return response.json();
                })
                .then(data => {
                    chapterSelect.innerHTML = '<option value="">-- Chọn chương --</option>';
                    if (data && data.length > 0) {
                        // Sửa tên biến `chapterId` thành `chapter` để rõ ràng hơn
                        data.forEach(chapter => {
                            const option = document.createElement('option');
                            option.value = chapter.id;
                            option.textContent = chapter.title;
                            chapterSelect.appendChild(option);
                        });
                    } else {
                        chapterSelect.innerHTML = '<option value="">-- Môn học này chưa có chương --</option>';
                    }
                    // Cập nhật lại trạng thái nút sau khi tải xong
                    updateSubmitButtonState();
                })
                .catch(error => {
                    chapterSelect.innerHTML = '<option value="">-- Lỗi tải chương --</option>';
                    console.error('Fetch error:', error);
                    updateSubmitButtonState(); // Giữ nút bị vô hiệu hóa
                });
        });

        // Sự kiện khi thay đổi chương
        chapterSelect.addEventListener('change', updateSubmitButtonState);
    });
</script>
</body>
</html>