<%@ page import="org.example.rf.model.User" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%
    String uri1 = request.getRequestURI();
    boolean skipFooter = uri1.contains("login.jsp") || uri1.contains("register.jsp");

    if (!skipFooter) {
%>
<footer>
    <p>&copy; 2025 EduPlatform. All rights reserved.</p>
</footer>
<%
    }
%>
