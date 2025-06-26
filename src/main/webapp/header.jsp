<%@ page import="org.example.rf.model.User" pageEncoding="UTF-8" %>

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
                        <a class="nav-link <%= uri.contains("ai-agent") ? "active" : "" %>" href="<%= request.getContextPath() %>/ai-agent">AI Agent</a>
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
                <form class="d-flex searchform" role="search" action="#" onsubmit="return false;">
                    <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                    <button class="btn btn-primary" type="submit" id="search-btn"><i class="fa fa-search"></i></button>
                </form>
                <div class="auth-buttons ms-3">
                    <% if (user == null) { %>
                        <a href="<%= request.getContextPath() %>/register" class="btn logup-button">Sign Up</a>
                        <a href="<%= request.getContextPath() %>/login" class="btn login-button">Login</a>
                    <% } else { %>
                        <a href="<%= request.getContextPath() %>/enrollment" class="btn logup-button">My Course</a>
                        <a href="<%= request.getContextPath() %>/user-info" class="btn login-button">👤 <%= user.getUserName() %></a>
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