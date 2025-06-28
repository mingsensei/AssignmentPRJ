<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<%--
  Created by IntelliJ IDEA.
  User: macos
  Date: 5/6/25
  Time: 15:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>My Courses</title>
    <link rel="stylesheet" href="css/enrollment.css">
</head>
<%@ include file="header.jsp" %>

<body>
<%@ include file="menu.jsp" %>
<div class="main-content d-flex flex-column min-vh-100">
  <div class="container">
    <div class="row">
      <c:forEach var="courseList" items="${courseList}" varStatus="status">
        <div class="col-12 mb-4 d-flex align-items-stretch">
          <div class="course-card w-100">
            <div class="course-header">
              <img src="${pageContext.request.contextPath}/images/course${courseList.id}.webp" alt="Course Image" class="course-image" />
              <div class="course-info">
                <h3 class="course-title"> ${courseList.name}</h3>
                <div class="chap_but">
                  <a>${courseList.description}</a>
                  <button class="toggle-btn" onclick="toggleChapters(this)">
                    <svg class="arrow-icon" width="24" height="24" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <polyline points="6 9 12 15 18 9"></polyline>
                    </svg>
                  </button>
                </div>
              </div>
              <div class="course-level">Intermediate</div>
            </div>
            <div class="chapter-progress">
              <c:forEach var="i" begin="1" end="${chapters[status.index]}">
                <div class="chapter-item">
                  <div class="chapter-circle ${i == 1 ? 'active' : ''}">${i}</div>
                </div>
              </c:forEach>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>

  <%@ include file="footer.jsp" %>
</div>
</body>

<script>
  function toggleChapters(button) {
    const progress = button.closest('.course-card').querySelector('.chapter-progress');

    if (progress.style.maxHeight && progress.style.maxHeight !== '0px') {
      // Đóng
      progress.style.transition = 'max-height 0.4s ease-out';
      progress.style.maxHeight = '0px';
      button.classList.remove('rotate');
    } else {
      // Mở
      progress.style.transition = 'max-height 0.4s ease-in';
      progress.style.maxHeight = progress.scrollHeight + 'px';
      button.classList.add('rotate');
    }
  }
</script>
