<%@ page import="org.example.rf.model.Plan" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>User Profile</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/user_info.css" />
</head>
<body>
<%@ include file="header.jsp" %>
<%
  User user = (User) request.getAttribute("user");
  Plan currentPlan = (Plan) request.getAttribute("currentPlan");
%>
<div class="container12">
  <div class="sidebar">
    <h2>User Panel</h2>
    <button class="active" onclick="openTab('profile', event)">Profile Info</button>
    <button onclick="openTab('security', event)">Security</button>
    <button onclick="openTab('plan', event)">My Plan</button>
    <button onclick="openTab('others', event)">Others</button>
  </div>

  <div class="content">
    <c:if test="${not empty message}">
      <div class="alert alert-success" role="alert" style="margin: 10px;">
        ${message}
      </div>
    </c:if>
    <c:if test="${not empty error}">
      <div class="alert alert-danger" role="alert" style="margin: 10px;">
        ${error}
      </div>
    </c:if>
    <div id="profile" class="tabcontent active">
      <h3>Profile Information</h3>
      <div class="profile-header">
        <img id="avatarImg" class="avatar" src="https://i.pravatar.cc/150" alt="Avatar" />
        <div class="info-text" id="infoDisplay">
          <p><strong>Full Name:</strong> <%= user.getLastName() %> <%= user.getFirstName() %></p>
          <p><strong>Email:</strong> <%= user.getEmail() %></p>
          <p><strong>Phone:</strong> <%= user.getPhone() != null ? user.getPhone() : "Not set" %></p>
          <p><strong>Username:</strong> <%= user.getUserName() %></p>
          <p><strong>Role:</strong> <%= user.getRole() %></p>
          <button class="edit-btn" onclick="toggleEdit(true)">Edit</button>
        </div>

        <form id="infoEdit" action="user-info"  class="edit-inf" method="post" style="display: none">
          <input type="hidden" name="action" value="updateInfo" />
          <label>Last Name:</label>
          <input type="text" name="lastName" value="<%= user.getLastName() %>" required><br>
          <label>First Name:</label>
          <input type="text" name="firstName" value="<%= user.getFirstName() %>" required><br>
          <label>Phone:</label>
          <input type="text" name="phone" value="<%= user.getPhone() != null ? user.getPhone() : "" %>"><br>
          <label>Username:</label>
          <input type="text" name="userName" value="<%= user.getUserName() %>" required><br>
          <button type="submit">Save</button>
          <button type="button" onclick="toggleEdit(false)">Cancel</button>
        </form>
      </div>
    </div>

    <div id="security" class="tabcontent">
      <h3>Security</h3>
      <form method="post" action="updatePassword">
        <label for="currentPass">Current Password</label>
        <input type="password" id="currentPass" name="currentPass" placeholder="Enter current password" />
        <label for="newPass">New Password</label>
        <input type="password" id="newPass" name="newPass" placeholder="Enter new password" />
        <label for="confirmPass">Confirm New Password</label>
        <input type="password" id="confirmPass" name="confirmPass" placeholder="Confirm new password" />
        <button type="submit">Update Password</button>
      </form>
    </div>

    <div id="plan" class="tabcontent">
      <h3>Current Subscription Plan</h3>
      <c:if test="${not empty currentPlan}">
        <div class="card" style="padding: 20px; border: 1px solid #ccc; border-radius: 10px;">
          <p><strong>Plan Name:</strong> ${currentPlan.name}</p>
          <p><strong>Start Date:</strong> ${currentSubscription.startDate}</p>
          <p><strong>End Date:</strong>
            <c:choose>
              <c:when test="${currentSubscription.endDate != null}">${currentSubscription.endDate}</c:when>
              <c:otherwise>Unlimited</c:otherwise>
            </c:choose>
          </p>
          <p><strong>AI Exams Used:</strong>
            <c:choose>
              <c:when test="${currentPlan.maxTestAttempts >= 999999}">${examCount} / Unlimited</c:when>
              <c:otherwise>${examCount} / ${currentPlan.maxTestAttempts}</c:otherwise>
            </c:choose>
          </p>
          <p><strong>Blog/Q&A Posts:</strong>
            <c:choose>
              <c:when test="${currentPlan.maxPosts == null}">${postCount} / Unlimited</c:when>
              <c:otherwise>${postCount} / ${currentPlan.maxPosts}</c:otherwise>
            </c:choose>
          </p>
        </div>
      </c:if>
      <c:if test="${empty currentPlan}">
        <p style="color: red;">You do not currently have a subscription plan.</p>
      </c:if>
      <br>
      <a href="${pageContext.request.contextPath}/plan-pricing" class="btn btn-primary">Upgrade Plan</a>
    </div>

    <div id="others" class="tabcontent">
      <h3>Others</h3>
      <form action="logout" method="post">
        <button class="logout-button" type="submit">Logout</button>
      </form>
    </div>
  </div>
</div>

<script>
  function openTab(tabId, event) {
    const tabs = document.querySelectorAll(".tabcontent");
    const buttons = document.querySelectorAll(".sidebar button");
    tabs.forEach(t => t.classList.remove("active"));
    buttons.forEach(b => b.classList.remove("active"));
    document.getElementById(tabId).classList.add("active");
    event.target.classList.add("active");
  }
  function toggleEdit(showEdit) {
    document.getElementById('infoDisplay').style.display = showEdit ? 'none' : 'block';
    document.getElementById('infoEdit').style.display = showEdit ? 'block' : 'none';
    document.getElementById('avatarImg').style.display = showEdit ? 'none' : 'block';
  }

</script>
<%@ include file="footer.jsp" %>
</body>
</html>
