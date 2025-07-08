<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>

<head>
    <title>Chọn số câu hỏi và độ khó</title>
</head>
<body>

<%-- ✅ Hiển thị thông báo lỗi nếu có --%>
<c:if test="${not empty errorMessage}">
    <div style="color:red; margin-bottom: 10px; border: 1px solid red; padding: 10px;">
        <strong>Lỗi:</strong> ${errorMessage}<br>

        <c:if test="${fn:contains(errorMessage, 'Limit')}">
            <p>Vui lòng <a href="${pageContext.request.contextPath}/plan-pricing">nâng cấp gói</a> để tiếp tục sử dụng đầy đủ tính năng.</p>
        </c:if>
    </div>
</c:if>

<h1>Cài đặt bài kiểm tra</h1>
<form action="${pageContext.request.contextPath}/exam" method="post">
    <label for="numQuestionsParam">Số câu hỏi cần làm:</label>
    <input type="hidden" name="chapterId" value="${chapterId}">

    <input type="number" id="numQuestionsParam" name="numQuestionsParam" required><br><br>

    <label for="difficulty">Độ khó:</label>
    <select id="difficulty" name="difficulty" required>
        <option value="">--Chọn độ khó--</option>
        <option value="ai">AI tự xác định</option>
        <option value="veryEasy">Rất dễ</option>
        <option value="easy">Dễ</option>
        <option value="medium">Trung bình</option>
        <option value="hard">Khó</option>
        <option value="veryHard">Rất khó</option>
    </select><br><br>

    <input type="submit" value="Bắt đầu làm bài">
</form>
</body>
</html>
