<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@ page import="org.example.rf.model.User" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Trang Quản Lý Thông Tin Cá Nhân</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/user_info.css" />
</head>
<%@ include file="header.jsp" %>
<body>
<%
  User user = (User) request.getAttribute("user");
%>

<div class="container12">
  <div class="sidebar">
    <h2>Trang cá nhân</h2>
    <button class="active" onclick="openTab('profile', event)">Thông tin cá nhân</button>
    <button onclick="openTab('security', event)">Bảo mật</button>
    <button onclick="openTab('courses', event)">Khoá học đã mua</button>
    <button onclick="openTab('others', event)">Khác</button>
  </div>

  <div class="content">
    <div id="profile" class="tabcontent active">
      <h3>Thông tin cá nhân</h3>
      <div class="profile-header">
        <img class="avatar" src="https://i.pravatar.cc/150" alt="Ảnh đại diện" />

        <!-- VIEW MODE -->
        <div class="info-text" id="infoDisplay">
          <p><strong>Họ tên:</strong> <%= user.getLastName() %> <%= user.getFirstName() %></p>
          <p><strong>Email:</strong> <%= user.getEmail() %></p>
          <p><strong>Số điện thoại:</strong> <%= user.getPhone() != null ? user.getPhone() : "Chưa cập nhật" %></p>
          <p><strong>Tên người dùng:</strong> <%= user.getUserName() %></p>
          <p><strong>Vai trò:</strong> <%= user.getRole() %></p>
          <button class="edit-btn" onclick="toggleEdit(true)">Chỉnh sửa thông tin</button>
        </div>

        <!-- EDIT MODE (ẩn ban đầu) -->
        <form id="infoEdit" action="user-info" method="post" style="display: none">
          <input type="hidden" name="action" value="updateInfo" />
          <label>Họ:</label>
          <input type="text" name="lastName" value="<%= user.getLastName() %>" required><br>

          <label>Tên:</label>
          <input type="text" name="firstName" value="<%= user.getFirstName() %>" required><br>

          <label>Số điện thoại:</label>
          <input type="text" name="phone" value="<%= user.getPhone() != null ? user.getPhone() : "" %>"><br>

          <label>Tên người dùng:</label>
          <input type="text" name="userName" value="<%= user.getUserName() %>" required><br>

          <button type="submit">Lưu thay đổi</button>
          <button type="button" onclick="toggleEdit(false)">Hủy</button>
        </form>

      </div>

    </div>

    <div id="security" class="tabcontent">
      <h3>Bảo mật</h3>
      <form method="post" action="updatePassword">
        <label for="currentPass">Mật khẩu hiện tại</label>
        <input type="password" id="currentPass" name="currentPass" placeholder="Nhập mật khẩu hiện tại" />

        <label for="newPass">Mật khẩu mới</label>
        <input type="password" id="newPass" name="newPass" placeholder="Nhập mật khẩu mới" />

        <label for="confirmPass">Xác nhận mật khẩu mới</label>
        <input type="password" id="confirmPass" name="confirmPass" placeholder="Xác nhận mật khẩu mới" />

        <button type="submit">Cập nhật mật khẩu</button>
      </form>
    </div>

    <div id="courses" class="tabcontent">
      <h3>Khoá học đã mua</h3>
      <ul>
        <li>Khóa học Java căn bản</li>
        <li>Khóa học Web Fullstack</li>
        <li>Khóa học AI căn bản</li>
      </ul>
    </div>

    <div id="others" class="tabcontent">
      <h3>Khác</h3>
      <form action="logout" method="post">
        <button class="logout-button" type="submit">Đăng xuất</button>
      </form>
    </div>
  </div>
</div>

<script src="<%= request.getContextPath() %>/js/userinfo.js"></script>

</body>
<%@ include file="footer.jsp" %>
</html>
