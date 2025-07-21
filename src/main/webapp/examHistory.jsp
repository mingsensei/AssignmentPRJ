<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Lịch sử làm bài</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background-color: #f4f6f9; margin: 0; padding: 40px; }
        .container { max-width: 900px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h1 { text-align: center; color: #333; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #007bff; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
        a { color: #007bff; text-decoration: none; }
        a:hover { text-decoration: underline; }
        .back-link { display: inline-block; margin-top: 20px; font-weight: bold; }
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <h1>Lịch sử làm bài</h1>
    <c:choose>
        <c:when test="${not empty examHistory}">
            <table>
                <thead>
                <tr>
                    <th>Mã bài thi</th>
                    <th>Chương</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="exam" items="${examHistory}">
                    <tr>
                        <td>${exam.id}</td>
                        <td>${exam.chapterId}</td>
<%--                        <td>--%>
<%--                            <fmt:formatDate value="${exam.submmitedAt}" pattern="HH:mm, dd/MM/yyyy" />--%>
<%--                        </td>--%>
                        <td>
                                <a href="${pageContext.request.contextPath}/review?examid=${exam.id}">Xem chi tiết</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p style="text-align: center; margin-top: 30px;">Bạn chưa làm bài kiểm tra nào.</p>
        </c:otherwise>
    </c:choose>
    <a href="${pageContext.request.contextPath}/exam/setup/demo" class="back-link">← Quay lại</a>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>