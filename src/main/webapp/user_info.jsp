<%@ page import="org.example.rf.model.Plan" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>User Profile</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/css/user_info.css" />
        <style>
            .container12 {
                display: flex;
                min-height: 80vh;
                margin: 30px;
                border: 1px solid #ddd;
                border-radius: 10px;
                overflow: hidden;
                background-color: #fff;
            }
            .sidebar {
                width: 220px;
                background-color: #f8f9fa;
                padding: 20px;
                border-right: 1px solid #ddd;
            }
            .sidebar h2 {
                font-size: 18px;
                margin-bottom: 20px;
            }
            .sidebar button {
                display: block;
                width: 100%;
                margin-bottom: 10px;
                background-color: #e9ecef;
                border: none;
                padding: 10px;
                border-radius: 5px;
                text-align: left;
            }
            .sidebar button.active,
            .sidebar button:hover {
                background-color: #0d6efd;
                color: white;
            }
            .content {
                flex: 1;
                padding: 30px;
            }
            .tabcontent {
                display: none;
            }
            .tabcontent.active {
                display: block;
            }
            .profile .avatar {
                width: 100px;
                height: 100px;
                border-radius: 50%;
                margin-bottom: 15px;
            }
            .edit-inf input {
                margin-bottom: 10px;
                width: 100%;
            }
        </style>
    </head>
    <body>

        <%@ include file="header.jsp" %>
        <%
            Plan currentPlan = (Plan) request.getAttribute("currentPlan");%>

        <div class="container12">
            <div class="sidebar">
                <h2>User Panel</h2>
                <button class="active" onclick="openTab('profile', event)">Profile Info</button>
                <c:if test="${isOwner}">
                    <button onclick="openTab('security', event)">Security</button>
                    <button onclick="openTab('plan', event)">My Plan</button>
                    <button onclick="openTab('others', event)">Others</button>
                </c:if>
            </div>

            <div class="content">
                <c:if test="${not empty message}">
                    <div class="alert alert-success" role="alert">
                        ${message}
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>

                <div id="profile" class="tabcontent active">
                    <h3>Profile Information</h3>
                    <img id="avatarImg" class="avatar" src="${(user.profilePic!=null)?user.profilePic:"https://i.pravatar.cc/150"}" alt="Avatar" />
                    <div class="info-text" id="infoDisplay">
                        <p><strong>Full Name:</strong> ${ user.lastName} ${user.firstName}</p>
                        <p><strong>Email:</strong> ${ user.email}</p>
                        <p><strong>Phone:</strong> ${ user.phone != null ? user.phone : "Not set"}</p>
                        <p><strong>Username:</strong> ${ user.userName}</p>
                        <p><strong>Role:</strong> ${ user.role}</p>
                        <c:if test="${isOwner}">
                            <button class="btn btn-outline-primary mt-2" onclick="toggleEdit(true)">Edit</button>
                        </c:if>
                    </div>
                    <c:if test="${isOwner}">
                        <form id="infoEdit" action="user-info" method="post" style="display: none" class="edit-inf">
                            <input type="hidden" name="action" value="updateInfo" />
                            <label>Last Name:</label>
                            <input class="form-control" type="text" name="lastName" value="${ user.lastName}" required>
                            <label>First Name:</label>
                            <input class="form-control" type="text" name="firstName" value="${ user.firstName}" required>
                            <label>Phone:</label>
                            <input class="form-control" type="text" name="phone" value="${ user.phone != null ? user.phone : ""}">
                            <label>Username:</label>
                            <input class="form-control" type="text" name="userName" value="${ user.userName}" required>
                            <label>Profile Picture:</label>
                            <input class="form-control" type="text" name="profilePic" value="${ user.profilePic!= null ? user.profilePic: ""}">
                            <div class="mt-2">
                                <button class="btn btn-success" type="submit">Save</button>
                                <button class="btn btn-secondary" type="button" onclick="toggleEdit(false)">Cancel</button>
                            </div>
                        </form>
                    </c:if>
                </div>

                <c:if test="${isOwner}">
                    <div id="security" class="tabcontent">
                        <h3>Security</h3>
                        <form method="post" action="updatePassword">
                            <label>Current Password</label>
                            <input type="password" name="currentPass" class="form-control" />
                            <label>New Password</label>
                            <input type="password" name="newPass" class="form-control" />
                            <label>Confirm New Password</label>
                            <input type="password" name="confirmPass" class="form-control" />
                            <button class="btn btn-primary mt-2" type="submit">Update Password</button>
                        </form>
                    </div>

                    <div id="plan" class="tabcontent">
                        <h3>Current Subscription Plan</h3>
                        <c:if test="${not empty currentPlan}">
                            <div class="card p-3">
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
                            <p class="text-danger">You do not currently have a subscription plan.</p>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/plan-pricing" class="btn btn-primary mt-3">Upgrade Plan</a>
                    </div>

                    <div id="others" class="tabcontent">
                        <h3>Others</h3>
                        <form action="logout" method="post">
                            <button class="btn btn-danger" type="submit">Logout</button>
                        </form>
                    </div>
                </c:if>
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
