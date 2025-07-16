<%@ page import="org.example.rf.model.User" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String uri = request.getRequestURI();
    boolean skipHeader = uri.contains("login") || uri.contains("register") ;

    if (!skipHeader) {
        HttpSession session1 = request.getSession(false);
        User user = (session1 != null) ? (User) session1.getAttribute("user") : null;
%>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">

    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/footer.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/header.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/effect.css">

</head>
<style>
    a{
        text-decoration: none !important;
    }
</style>

<header>
    <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top" id="main-navbar" style="background: rgb(240, 248, 255); height: 90px; padding: 15px 40px;">
        <div class="container-fluid">
            <a class="navbar-brand" href="<%= request.getContextPath() %>/home">NMQ AI <span>Education</span></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0" style="margin-left: 10%;gap: 20px;"
                >
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
                        <a href="<%= request.getContextPath() %>/enrollment" class="btn logup-button">My Course</a>
                    <div class="user-dropdown" style="position: relative; display: inline-block;">
                        <c:choose>
                            <c:when test="${sessionScope.currentPlan.name == 'Personal'}">
                                <a href="${pageContext.request.contextPath}/user-info"
                                   class="btn plan-personal user-btn"
                                   style="background-color: #ffc107; color: black;">
                                    👤 ${sessionScope.user.userName}
                                </a>
                            </c:when>
                            <c:when test="${sessionScope.currentPlan.name == 'Ultimate'}">
                                <a href="${pageContext.request.contextPath}/user-info"
                                   class="btn plan-ultimate user-btn"
                                   style="background-color: #6f42c1; color: white;">
                                    👤 ${sessionScope.user.userName}
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/user-info"
                                   class="btn plan-free user-btn">
                                    👤 ${sessionScope.user.userName}
                                </a>
                            </c:otherwise>
                        </c:choose>
                        <!-- Dropdown menu -->
                        <div class="dropdown-menu" >
                            <a href="${pageContext.request.contextPath}/logout"
                               style="display: block; padding: 10px 16px; color: #333; text-decoration: none;">
                                Đăng xuất
                            </a>
                        </div>
                    </div>

                    <% } %>
                </div>
            </div>
        </div>
    </nav>
    <!-- Bootstrap 5 JS Bundle CDN (Popper included) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</header>
<%
    } // đóng if (!skipHeader)
%>
<script src="<%= request.getContextPath() %>/js/effect.js"></script>

<script>
    // Sửa lại đoạn script cho Bootstrap navbar
    const navbar = document.getElementById('main-navbar');
    let lastScrollTop = 0;
    const threshold = 80;
    window.addEventListener('scroll', () => {
        const currentScroll = window.scrollY;
        if (currentScroll > threshold && currentScroll > lastScrollTop) {
            navbar.classList.add('scrolled');
            navbar.classList.add('hide');
        } else {
            navbar.classList.remove('hide');
            if (currentScroll <= threshold) {
                navbar.classList.remove('scrolled');
            }
        }
        lastScrollTop = currentScroll <= 0 ? 0 : currentScroll;
    });



    const searchForm = document.querySelector('.searchform');
    const searchInput = searchForm.querySelector('input');

    searchForm.addEventListener('mouseenter', () => {
        setTimeout(() => {
            searchInput.focus();
        }, 200); // delay nhỏ để input hiện ra trước khi focus
    });


</script>