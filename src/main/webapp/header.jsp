<%@ page import="org.example.rf.model.User" pageEncoding="UTF-8" %>

<%
    String uri = request.getRequestURI();
    boolean skipHeader = uri.contains("login.jsp") || uri.contains("register.jsp");

    if (!skipHeader) {
        HttpSession session1 = request.getSession(false);
        User user = (session1 != null) ? (User) session1.getAttribute("user") : null;
%>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">

    <!-- Font Awesome (cho icon search) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/footer.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/header.css">


</head>

<header>
    <nav class="navbar" id="main-navbar">
        <a class="navbar-brand" href="<%= request.getContextPath() %>/">NMQ AI <span>Education</span></a>

        <ul>
            <li><a href="<%= request.getContextPath() %>/courses">Course</a></li>
            <li><a href="<%= request.getContextPath() %>/ai-agent">AI Agent</a></li>
            <li><a href="<%= request.getContextPath() %>/exam">AI Exam</a></li>
            <li><a href="<%= request.getContextPath() %>/blog">Blog</a></li>
            <li><a href="<%= request.getContextPath() %>/about">About</a></li>
        </ul>

        <div class="searchform" action="#">
            <input type="text" placeholder="Search">
            <button type="submit" id="search-btn"><i class="fa fa-search"></i></button>
        </div>


        <%
            User user1 = (User)session.getAttribute("user");
            if (user == null) {
        %>
        <div class="auth-buttons">
            <a href="<%= request.getContextPath() %>/register" class="logup-button">Sign Up</a>
            <a href="<%= request.getContextPath() %>/login" class="login-button">Login</a>
        </div>

        <%
        } else {
            String userName = "";
            try {
                userName = user1.getUserName();
            } catch (Exception e) {
                userName = "User";
            }
        %>
        <div class="auth-buttons">
            <a href="<%= request.getContextPath() %>/user-info" class="login-button">
                👤 <%= userName %>
            </a>
        </div>

        <%
            }
        %>
    </nav>
</header>
<%
    } // đóng if (!skipHeader)
%>

<script>
    // Sửa lại đoạn script
    const navbar = document.getElementById('main-navbar');
    let lastScrollTop = 0;
    const threshold = 80; // ngưỡng cuộn mới (tùy chỉnh cao hơn nếu cần)

    window.addEventListener('scroll', () => {
        const currentScroll = window.scrollY;

        if (currentScroll > threshold && currentScroll > lastScrollTop) {
            // cuộn xuống mạnh hơn ngưỡng => ẩn navbar
            navbar.classList.add('scrolled');
            navbar.classList.add('hide');
        } else {
            // cuộn lên hoặc cuộn nhẹ => hiện navbar
            navbar.classList.remove('hide');

            // Nếu gần đầu trang thì bỏ hiệu ứng "scrolled"
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

