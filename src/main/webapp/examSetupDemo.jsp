<%@ page import="org.example.rf.model.Course" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Kiểm tra trắc nghiệm</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 40px;
        }

        .container {
            max-width: 700px;
            margin: auto;
            background-color: #ffffff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            position: relative;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
        }

        input, select {
            width: 100%;
            padding: 10px;
            font-size: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        button[type="submit"] {
            padding: 10px 20px;
            font-size: 16px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }

        button[type="submit"]:hover {
            background-color: #0056b3;
        }

        .upload-button-wrapper {
            position: absolute;
            top: 30px;
            right: 30px;
        }

        .upload-button-wrapper a {
            text-decoration: none;
            padding: 10px 18px;
            background-color: #28a745;
            color: white;
            border-radius: 6px;
            font-size: 14px;
            font-weight: bold;
        }

        .upload-button-wrapper a:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">

    <!-- Nút thêm tài liệu bên phải -->
    <div class="upload-button-wrapper">
        <a href="${pageContext.request.contextPath}/plan-pricing">PLan</a>
        <a href="${pageContext.request.contextPath}/exam/history">Xem lịch sử</a>
    </div>

</div>

<div style="margin-top: 30px; text-align: center;">
    <a href="${pageContext.request.contextPath}/home" style="
        display: inline-block;
        padding: 10px 20px;
        background-color: #6c757d;
        color: white;
        text-decoration: none;
        border-radius: 6px;
        font-weight: bold;
    ">← Về trang chủ</a>
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
<%@ include file="footer.jsp" %>
</body>
</html>
