<%@ page import="org.example.rf.model.User" pageEncoding="UTF-8" %>

<%
    String uri = request.getRequestURI();
    boolean skipHeader = uri.contains("login.jsp") || uri.contains("register.jsp");

    if (!skipHeader) {
        HttpSession session1 = request.getSession(false);
        User user = (session1 != null) ? (User) session1.getAttribute("user") : null;
%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Source+Serif+Pro:400,600&display=swap" rel="stylesheet">

    <!-- Icon font -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/fonts/icomoon/style.css">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.min.css">

    <!-- Custom Style CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/footer.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/header.css">
</head>

<header>
    <section class="ftco-section">


        <nav class="navbar navbar-expand-lg navbar-dark ftco_navbar bg-dark ftco-navbar-light" id="ftco-navbar">
            <div class="container">
                <a class="navbar-brand" href="<%= request.getContextPath() %>/">NMQ AI <span>Education</span></a>
                <form action="#" class="searchform order-sm-start order-lg-last">
                    <div class="form-group d-flex">
                        <input type="text" class="form-control pl-3" placeholder="Search">
                        <button type="submit" placeholder="" class="form-control search"><span class="fa fa-search"></span></button>
                    </div>
                </form>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#ftco-nav" aria-controls="ftco-nav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="fa fa-bars"></span> Menu
                </button>
                <div class="collapse navbar-collapse" id="ftco-nav">
                    <ul class="navbar-nav m-auto">
                        <li class="nav-item"><a href="<%= request.getContextPath() %>/courses" class="nav-link">Course</a></li>
                        <li class="nav-item"><a href="<%= request.getContextPath() %>/ai-agent" class="nav-link">AI Agent</a></li>
                        <li class="nav-item"><a href="<%= request.getContextPath() %>/exam" class="nav-link">AI Exam</a></li>
                        <li class="nav-item"><a href="<%= request.getContextPath() %>/blog" class="nav-link">Blog</a></li>
                        <li class="nav-item"><a href="<%= request.getContextPath() %>/about" class="nav-link">About</a></li>
                    </ul>
                </div>
            </div>
            <%
                User user1 = (User)session.getAttribute("user");
                if (user == null) {
            %>
            <a href="<%= request.getContextPath() %>/register" class="logup-button">Sign Up</a>
            <a href="<%= request.getContextPath() %>/login" class="login-button">Login</a>
            <%
            } else {
                String userName = "";
                try {
                    userName = user1.getUserName();
                } catch (Exception e) {
                    userName = "User";
                }
            %>
            <a href="<%= request.getContextPath() %>/user-info" class="login-button">
                👤 <%= userName %>
            </a>
            <%
                }
            %>


        </nav>
        <!-- END nav -->

    </section>

</header>
<main style="padding: 1rem;">
        <%
    } // đóng if (!skipHeader)
%>
