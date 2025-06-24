<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Chi tiết khóa học - ${course.name}</title>
  <style>
    body {
      margin: 0; padding: 0;
      background-color: #f0f4fa;
      color: #333;
    }
    .price_buy{
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 30px;
    }

    .hero {
      display: flex;
      flex-wrap: wrap;
      background-color: white;
      max-width: 1200px;
      margin: 40px auto 30px;
      border-radius: 12px;
      box-shadow: 0 5px 15px rgba(0,0,0,0.1);
      overflow: hidden;
    }
    .hero img {
      flex: 1 1 400px;
      max-width: 700px;
      max-height: 420px;
      object-fit: cover;
    }
    .hero-info {
      flex: 1 1 300px;
      padding: 30px 40px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }
    .course-name {
      font-size: 2.2rem;
      font-weight: 700;
      color: #007bff;
      margin-bottom: 10px;
    }
    .course-description {
      font-size: 1.1rem;
      line-height: 1.5;
      color: #555;
      margin-bottom: 25px;
    }
    .course-price {
      font-size: 1.6rem;
      font-weight: 700;
      color: #007bff;
      background-color: #e1f0ff;
      padding: 12px 20px;
      border-radius: 10px;
      align-self: flex-start;
    }

    /* ===== DANH SÁCH CHAPTER ===== */
    .chapter-list {
      max-width: 1200px;
      margin: 0 auto 50px;
      padding: 0 20px;
    }
    .chapter-title {
      font-size: 1.8rem;
      font-weight: 700;
      color: #007bff;
      margin-bottom: 20px;
      text-align: center;
    }
    .chapter-item {
      background: white;
      margin-bottom: 15px;
      border-radius: 10px;
      box-shadow: 0 3px 8px rgba(0,0,0,0.08);
      padding: 20px 25px;
      transition: box-shadow 0.3s ease;
    }
    .chapter-item:hover {
      box-shadow: 0 6px 15px rgba(0,0,0,0.15);
    }
    .chapter-name {
      font-weight: 600;
      font-size: 1.2rem;
      color: #007bff;
      margin-bottom: 8px;
    }

    /* Responsive */
    @media (max-width: 900px) {
      .hero {
        flex-direction: column;
        max-width: 95%;
      }
      .hero-image, .hero-info {
        max-width: 100%;
        flex: none;
      }
      .hero-info {
        padding: 20px;
      }
    }

    .buy-now-btn {
      text-decoration: none;
      background-color: #1E90FF;
      color: white;
      padding: 10px 20px;
      font-size: 24px;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      transition: background-color 0.3s ease;
      height: 62px;
      width: 150px;
    }

    .buy-now-btn:hover {
      background-color: #0d75d8;
    }
  </style>
</head>
<%@ include file="header.jsp" %>

<body>

<div class="hero">
  <img src="${pageContext.request.contextPath}/images/course${course.id}.webp" alt="${course.name}">
  <div class="hero-info">
    <div>
      <div class="course-name">${course.name}</div>
      <div class="course-description">${course.description}</div>
    </div>
    <div class="price_buy">



      <c:choose>

      <c:when test="${enrollments.contains(course.id)}">
        <form action="${pageContext.request.contextPath}/mycourse?courseId=1&chapterId=?" method="get">
          <input type="hidden" name="courseId" value="${course.id}">
          <button type="submit" class="buy-now-btn">Enter Course</button>
        </form>
      </c:when>


      <c:otherwise>
        <div class="course-price">${course.price} VNĐ</div>
        <form action="${pageContext.request.contextPath}/add-to-cart" method="post">
          <input type="hidden" name="courseId" value="${course.id}">
          <button type="submit" class="buy-now-btn">Buy Now</button>
        </form>
      </c:otherwise>
      </c:choose>

    </div>
  </div>
</div>

<div class="chapter-list">
  <div class="chapter-title">Danh sách các chương</div>
  <c:forEach var="chapter" items="${chapters}">
    <div class="chapter-item">
      <div class="chapter-name">${chapter.title}</div>

    </div>
  </c:forEach>
</div>

</body>
<%@ include file="footer.jsp" %>
</html>
