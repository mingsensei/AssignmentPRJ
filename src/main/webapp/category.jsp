<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="currentPath" value="${url}" />
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

<div class="search-bar-wrap">
    <form id="searchForm" action="search" method="post" autocomplete="off">
        <div class="modern-search-bar">
            <input type="text" id="searchInput"
                   name="q"
                   placeholder="Nhập khoá học, chủ đề hoặc kỹ năng..."
                   required
                   autofocus
            >
            <button id="searchBtn" type="submit">
                <span style="font-size:1.13em; vertical-align: middle;">
                  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" style="margin-bottom:3px" viewBox="0 0 16 16">
                    <path d="M11 6a5 5 0 1 1-2-3.874A5 5 0 0 1 11 6zm1.318 7.318a1 1 0 0 0 1.364-1.364l-3.122-3.122A6.983 6.983 0 0 0 13 6a7 7 0 1 0-7 7 6.983 6.983 0 0 0 3.832-1.06l3.122 3.122z"/>
                  </svg>
                </span>
                <span class="d-none d-md-inline ms-1">SEARCH</span>
            </button>
        </div>
    </form>
    <div class="search-note">Ví dụ: Java Web, Data Science, AI cơ bản, Lập trình Python...</div>
</div>
<c:if test="${fn:contains(currentPath, 'category')}">
    <!-- Carousel code ở đây -->
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
</c:if>


<c:choose>
    <c:when test="${fn:contains(currentPath, '/search') and empty courses}">
        <!-- Thông báo khi không có khóa học -->
        <div class="col-12 text-center py-5">
            <div style="padding: 30px; background-color: #E6F7FF; border: 2px dashed #3399FF; border-radius: 12px;">
                <h3 style="color: #3399FF; font-weight: 600;">Seems that we had no course that suitable</h3>
                <p style="color: #666; margin-top: 8px;">Let's find some thing else..</p>
                <a href="${pageContext.request.contextPath}/category">
                    <button class="btn btn-primary mt-3 px-4 py-2">Explore Courses</button>
                </a>
            </div>
        </div>
    </c:when>

    <c:otherwise>
        <!-- Danh sách khóa học -->
        <div class="container py-5" style="max-width:950px;">
            <c:if test="${fn:contains(currentPath, '/search') }">
                <h2 class="mb-4 fw-bold text-primary" style="font-size:2.1rem">Search result: </h2>
            </c:if>
            <c:forEach var="course" items="${courses}">
            <a href="<%= request.getContextPath() %>/course?courseId=${course.id}">
                <div class="mycourse-result-card">
                    <img src="${pageContext.request.contextPath}/images/course${course.id}.webp"
                         class="mycourse-thumb"
                         alt="${course.name}">
                    <div class="mycourse-info">
                        <div class="mycourse-title">${course.name}</div>
                        <div class="mycourse-desc">${course.description}</div>
                    </div>
                </div>
            </a>
            </c:forEach>
        </div>

    </c:otherwise>
</c:choose>
<h2>All Courses</h2>
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
