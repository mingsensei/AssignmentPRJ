<%@ page language="java" contentType="text/html;
charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, org.example.rf.model.Course" %>
<%@ page import="org.example.rf.model.Course" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Upload PDF Material</title>
    <style>
        /* Giữ nguyên style cũ */
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .container {
            width: 600px;
            margin: 0 auto;
            border: 1px solid #ddd;
            padding: 20px;
            border-radius: 5px;
        }
        h1 {
            text-align: center;
            color: #333;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="text"], select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type="file"] {
            padding: 8px 0;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #45a049;
        }
        .error {
            color: red;
            font-weight: bold;
            margin-bottom: 15px;
        }
        .success {
            color: green;
            font-weight: bold;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Upload PDF Material</h1>

    <form action="<%= request.getContextPath() %>/material/uploads" method="post" enctype="multipart/form-data">
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
                <option value="">-- Chọn chương --</option>
            </select>
        </div>

        <div class="form-group">
            <label for="pdfFile">Chọn file PDF:</label>
            <input type="file" id="pdfFile" name="pdfFile" accept=".pdf" required>
        </div>

        <div class="form-group">
            <button type="submit">Tải lên</button>
        </div>
    </form>
</div>

<script>
    document.getElementById('courseId').addEventListener('change', function () {
        const courseId = this.value;
        const chapterSelect = document.getElementById('chapterId');

        chapterSelect.innerHTML = '<option value="">-- Đang tải chương --</option>';

        if (!courseId) {
            chapterSelect.innerHTML = '<option value="">-- Chọn chương --</option>';
            return;
        }

        fetch('<%=request.getContextPath()%>/chapters-by-course?courseId=' + encodeURIComponent(courseId))
            .then(response => {
                if (!response.ok) {
                    throw new Error('Lỗi khi tải chương');
                }
                return response.json();
            })
            .then(data => {
                chapterSelect.innerHTML = '<option value="">-- Chọn chương --</option>';
                data.forEach(chapterId => {
                    const option = document.createElement('option');
                    option.value = chapterId.id;
                    option.textContent = chapterId.title;
                    chapterSelect.appendChild(option);
                });
            })
            .catch(error => {
                chapterSelect.innerHTML = '<option value="">-- Lỗi tải chương --</option>';
                console.error(error);
            });
    });
</script>
</body>
</html>