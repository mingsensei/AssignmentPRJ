<%@ page import="org.example.rf.model.User" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%
    String uri = request.getRequestURI();
    boolean skipHeader = uri.contains("login.jsp") || uri.contains("register.jsp");

    if (!skipHeader) {
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/header.css" />
</head>
<header>
    <nav class="navbar">
        <div class="logo">EduPlatform</div>
        <ul class="nav-links">
            <li><a href="<%= request.getContextPath() %>/courses">Môn học</a></li>
            <li><a href="course">Kiểm tra</a></li>
            <li><a href="#">AI Agent</a></li>
            <li>
                <%
                    User user = (User) session.getAttribute("user");
                    if (user != null) {
                %>
                <a href="user-info">Xin chào, <%= user.getUserName()%></a>
                <%
                } else {
                %>
                <a href="<%= request.getContextPath() %>/login">Đăng nhập</a>
                <%
                    }
                %>
            </li>
        </ul>
    </nav>
</header>
    <%
    } // end if (!skipHeader)
%>
