<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>${blog.title}</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/css/index.css" />
        <style>
            body {
                margin: 0;
                font-family: 'Segoe UI', sans-serif;
                background-color: #0f172a;
                color: #f1f5f9;
            }

            .blog-container {
                max-width: 800px;
                margin: 0 auto;
                padding: 2rem 1rem;
            }

            .blog-breadcrumb {
                font-size: 0.9rem;
                color: #9ca3af;
                margin-bottom: 1rem;
            }

            .blog-title {
                font-size: 2.5rem;
                font-weight: 700;
                margin-bottom: 1rem;
                color: #ffffff;
            }

            .blog-meta {
                display: flex;
                align-items: center;
                gap: 1rem;
                margin-bottom: 2rem;
                color: #d1d5db;
            }

            .blog-meta .author-avatar {
                width: 40px;
                height: 40px;
                border-radius: 9999px;
            }

            .blog-meta .author-info {
                display: flex;
                flex-direction: column;
            }

            .blog-meta .author-info .name {
                font-weight: 600;
                color: #ffffff;
            }

            .blog-excerpt {
                background-color: #1f2937;
                padding: 1rem;
                font-style: italic;
                border-radius: 8px;
                margin-bottom: 2rem;
                color: #e5e7eb;
            }

            .blog-image {
                width: 100%;
                max-height: 500px;
                object-fit: cover;
                border-radius: 12px;
                margin-bottom: 1.5rem;
            }

            .blog-description {
                font-size: 1.1rem;
                line-height: 1.7;
                color: #e2e8f0;
            }

            .blog-tag {
                background-color: #374151;
                padding: 0.25rem 0.75rem;
                border-radius: 9999px;
                font-size: 0.85rem;
                margin-bottom: 0.5rem;
                display: inline-block;
            }
            .blog-breadcrumb-link {
                color: inherit;
                text-decoration: none;
                transition: color 0.2s ease;
            }

            .blog-breadcrumb-link:hover {
                color: #60a5fa; /* Light blue (or pick any highlight color you like) */
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <div class="blog-container">
            <div class="blog-breadcrumb">
                <a href="index.jsp" class="blog-breadcrumb-link">🏠</a> &nbsp;&gt;&nbsp; 
                <a href="blog" class="blog-breadcrumb-link">Post</a> &nbsp;&gt; 
                ${blog.title}</div>

            <div class="blog-title">${blog.title}</div>

            <div class="blog-meta">
                <!--                <div class="blog-tag">AI</div>-->
                <img src="https://i.pravatar.cc/48" class="author-avatar" alt="Author"/>
                <c:forEach var="blogUser" items="${blogUsers}">
                    <div class="author-info">
                        
                        <div class="role">${blogUser.blogRole}</div>
                    </div>
                </c:forEach>
                <div>${blog.createdAt}</div>
            </div>

            <!--            <div class="blog-excerpt">
                            Mục tiêu của thử nghiệm là giả lập một developer chưa từng tiếp xúc với langchain, langgraph yêu cầu AI hướng dẫn code một agent AI hoàn chỉnh
                        </div>-->

            <img src="https://picsum.photos/1024/768" class="blog-image" alt="Blog Image"/>

            <div class="blog-description">${blog.content}</div>
        </div>
    </body>
</html>
