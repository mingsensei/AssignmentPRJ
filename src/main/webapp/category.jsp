<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Khoá học</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/category.css" />
</head>
<%@ include file="header.jsp" %>
<body>

<!-- ===== KHOÁ HỌC NỔI BẬT ===== -->
<h1>Khoá học nổi bật</h1>
<div class="carousel-container">
    <div class="carousel-track" id="carouselTrack">
        <c:forEach var="course" items="${featuredCourses}">
            <div class="carousel-card">
                <a href="course?courseId=${course.id}">
                    <img src="${pageContext.request.contextPath}/images/course${course.id}.webp" alt="${course.name}">
                    <div class="carousel-title">${course.name}</div>
                    <div class="carousel-description">${course.description}</div>
                </a>
            </div>
        </c:forEach>
    </div>
</div>
<div class="dots" id="carouselDots"></div>

<!-- ===== TẤT CẢ KHOÁ HỌC ===== -->
<h2>Tất cả khoá học</h2>
<div class="course-grid">
    <c:forEach var="category" items="${categoryList}">
        <div class="course-card">
            <a href="<%= request.getContextPath() %>/course?courseId=${category.id}">
                <img src="${pageContext.request.contextPath}/images/course${category.id}.webp" alt="${category.name}">
                <div class="course-info">
                    <div class="course-title">${category.name}</div>
                    <div class="course-description">${category.description}</div>
                </div>
            </a>
        </div>
    </c:forEach>
</div>

<script src="<%= request.getContextPath() %>/js/carousel.js"></script>

</body>
<%@ include file="footer.jsp" %>
</html>
