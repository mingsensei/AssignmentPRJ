<%@ page import="org.example.rf.model.Course" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Kiểm tra trắc nghiệm</title>
</head>
<body>
<div class="container">
    <form action="${pageContext.request.contextPath}/exam" method="post">
        <div class="form-group">
            <label for="courseId">Môn học:</label>
            <select id="courseId" name="courseId" required>
                <option value="">-- Chọn môn học --</option>
                <%
                    List<Course> courses = (List<Course>) request.getAttribute("courses");
                    if (courses != null) {
                        for (Course course : courses) {
                %>
                <option value="<%= course.getId() %>"><%= course.getName() %>
                </option>
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
            <label for="numQuestionsParam">Số câu hỏi:</label>
            <input type="number" id="numQuestionsParam" name="numQuestionsParam" required><br><br>
        </div>
        <div class="form-group">
            <select id="difficulty" name="difficulty" required>
                <option value="">--Chọn độ khó--</option>
                <option value="ai">AI tự xác định</option>
                <option value="veryEasy">Rất dễ</option>
                <option value="easy">Dễ</option>
                <option value="medium">Trung bình</option>
                <option value="hard">Khó</option>
                <option value="veryHard">Rất khó</option>
            </select><br><br>
        </div>
        <div class="form-group">
            <button type="submit">Bắt đầu</button>
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
