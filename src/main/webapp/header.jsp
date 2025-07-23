<%@ page import="org.example.rf.model.User" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String uri = request.getRequestURI();
    // Thêm check để không hiển thị header trên trang upload
    boolean skipHeader = uri.contains("login") || uri.contains("register") || uri.contains("upload-material");

    if (!skipHeader) {
        HttpSession session1 = request.getSession(false);
        User user = (session1 != null) ? (User) session1.getAttribute("user") : null;
%>
<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<div class="alert alert-danger" role="alert" style="margin: 10px;">
    <%= error %>
</div>
<%
    }
%>

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>NMQ AI Education</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/footer.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/header.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/effect.css">
</head>
<style>
    a { text-decoration: none !important; }
    /* Style cho dropdown menu */
    .user-dropdown .dropdown-menu {
        display: none;
        position: absolute;
        background-color: #f9f9f9;
        min-width: 160px;
        box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
        z-index: 1021; /* Cao hơn fixed-top navbar */
        border-radius: .25rem;
        right: 0; /* Căn phải */
    }
    .user-dropdown.show .dropdown-menu {
        display: block;
    }
</style>

<header>
    <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top" id="main-navbar" style="background: rgb(240, 248, 255); height: 90px; padding: 15px 40px;">
        <div class="container-fluid">
            <a class="navbar-brand" href="<%= request.getContextPath() %>/home">NMQ AI <span>Education</span></a>

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0" style="margin-left: 10%;gap: 20px;">
                    <li class="nav-item">
                        <a class="nav-link <%= uri.contains("category") || uri.contains("course") ? "active" : "" %>" href="<%= request.getContextPath() %>/category">Course</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= uri.contains("translate") ? "active" : "" %>" href="<%= request.getContextPath() %>/ai-translate">AI Translate</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= uri.contains("exam") ? "active" : "" %>" href="<%= request.getContextPath() %>/exam/setup/demo">AI Exam</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= uri.contains("blog") ? "active" : "" %>" href="<%= request.getContextPath() %>/blog">Blog</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= uri.contains("about") ? "active" : "" %>" href="<%= request.getContextPath() %>/about">About</a>
                    </li>
                </ul>

                <div class="auth-buttons ms-3">
                    <% if (user == null) { %>
                    <a href="<%= request.getContextPath() %>/register" class="btn logup-button">Sign Up</a>
                    <a href="<%= request.getContextPath() %>/login" class="btn login-button">Login</a>
                    <% } else { %>
                    <c:set var="user" value="${sessionScope.user}" />
                    <a href="<%= request.getContextPath() %>/enrollment" class="btn logup-button">My Course</a>

                    <div class="dropdown user-dropdown d-inline-block">
                        <c:choose>
                            <c:when test="${sessionScope.currentPlan.name == 'Personal'}">
                                <a href="#" class="btn plan-personal user-btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false"
                                   style="background-color: #ffc107; color: black;">
                                    <img class="avatar" src="${(user.profilePic!=null)?user.profilePic:"https://i.pravatar.cc/48"}">
                                    <c:choose>
                                        <c:when test="${not empty user.firstName and not empty user.lastName}">${user.firstName} ${user.lastName}</c:when>
                                        <c:when test="${not empty user.firstName}">${user.firstName}</c:when>
                                        <c:when test="${not empty user.lastName}">${user.lastName}</c:when>
                                        <c:otherwise>${user.userName}</c:otherwise>
                                    </c:choose>
                                </a>
                            </c:when>
                            <c:when test="${sessionScope.currentPlan.name == 'Ultimate'}">
                                <a href="#" class="btn plan-ultimate user-btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false"
                                   style="background-color: #6f42c1; color: white;">
                                    <img class="avatar" src="${(user.profilePic!=null)?user.profilePic:"https://i.pravatar.cc/48"}">
                                    <c:choose>
                                        <c:when test="${not empty user.firstName and not empty user.lastName}">${user.firstName} ${user.lastName}</c:when>
                                        <c:when test="${not empty user.firstName}">${user.firstName}</c:when>
                                        <c:when test="${not empty user.lastName}">${user.lastName}</c:when>
                                        <c:otherwise>${user.userName}</c:otherwise>
                                    </c:choose>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="#" class="btn plan-free user-btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                    <img class="avatar" src="${(user.profilePic!=null)?user.profilePic:"https://i.pravatar.cc/48"}">
                                    <c:choose>
                                        <c:when test="${not empty user.firstName and not empty user.lastName}">${user.firstName} ${user.lastName}</c:when>
                                        <c:when test="${not empty user.firstName}">${user.firstName}</c:when>
                                        <c:when test="${not empty user.lastName}">${user.lastName}</c:when>
                                        <c:otherwise>${user.userName}</c:otherwise>
                                    </c:choose>
                                </a>
                            </c:otherwise>
                        </c:choose>

                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/user-info">Thông tin cá nhân</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
                        </ul>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </nav>
</header>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="<%= request.getContextPath() %>/js/effect.js"></script>

<script>
    // Script cho hiệu ứng cuộn của navbar
    const navbar = document.getElementById('main-navbar');
    if (navbar) {
        let lastScrollTop = 0;
        const threshold = 80;
        window.addEventListener('scroll', () => {
            const currentScroll = window.scrollY;
            if (currentScroll > threshold && currentScroll > lastScrollTop) {
                navbar.classList.add('scrolled', 'hide');
            } else {
                navbar.classList.remove('hide');
                if (currentScroll <= threshold) {
                    navbar.classList.remove('scrolled');
                }
            }
            lastScrollTop = currentScroll <= 0 ? 0 : currentScroll;
        });
    }
</script>
<script src="js/InputValidator.js"></script>

<%
    } // đóng if (!skipHeader)
%>