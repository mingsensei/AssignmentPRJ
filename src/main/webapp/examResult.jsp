<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Kết Quả Bài Thi</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f8f9fa;
      padding: 40px;
    }
    .result-container {
      background-color: #fff;
      max-width: 500px;
      margin: auto;
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
      padding: 30px;
      text-align: center;
    }
    .result-container h1 {
      color: #333;
    }
    .result-container p {
      font-size: 18px;
      margin: 10px 0;
    }
    .score {
      font-size: 28px;
      color: #28a745;
      font-weight: bold;
    }
  </style>
</head>
<body>
<div class="result-container">
  <h1>Kết Quả Bài Thi</h1>

  <p><strong>Mã bài thi:</strong> ${examId}</p>
  <p><strong>Số câu hỏi:</strong> ${examSize}</p>
  <p><strong>Số câu trả lời đúng:</strong> <span class="score">${result}</span></p>

  <p>Chúc mừng bạn đã hoàn thành bài thi!</p>

  <a href="${pageContext.request.contextPath}/home.jsp">← Quay về trang chính</a>
</div>
</body>
</html>
