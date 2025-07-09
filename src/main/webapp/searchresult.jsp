<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Kết quả tìm kiếm</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/category.css" />
</head>
<%@ include file="header.jsp" %>
<body>
<div class="search-wrapper">
    <form class="searchform" action="search" method="get">
        <input type="search" name="query" placeholder="Tìm khoá học..." value="${query}" />
        <button type="submit">🔍</button>
    </form>
</div>
<h1>Kết quả tìm kiếm cho "<c:out value='${query}'/>"</h1>
<div class="course-grid">
    <c:forEach var="course" items="${courses}">
        <div class="course-card">
            <a href="<%= request.getContextPath() %>/course?courseId=${course.id}">
                <img src="<%= request.getContextPath() %>/images/course${course.id}.webp" alt="${course.name}">
                <div class="course-info">
                    <div class="course-title">${course.name}</div>
                    <div class="course-description">${course.description}</div>
                </div>
            </a>
        </div>
    </c:forEach>
    <c:if test="${empty courses}">
        <p>Không tìm thấy khóa học phù hợp.</p>
    </c:if>
</div>
</body>
<%@ include file="footer.jsp" %>
</html>
