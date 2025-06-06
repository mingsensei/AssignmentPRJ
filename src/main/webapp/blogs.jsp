
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Blog post</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/css/index.css" />
        <style>
            body {
                background-color: #0f172a; /* dark background */
                font-family: 'Segoe UI', sans-serif;
                color: #f1f5f9;
                margin: 0;
            }

            .blog-card {
                background-color: #1e293b;
                border: none;
                border-radius: 12px;
                padding: 16px;
                margin-bottom: 20px;
                max-width: 700px;
                text-align: left;
                box-shadow: 0 4px 14px rgba(0, 0, 0, 0.3);
                transition: transform 0.2s, box-shadow 0.2s;
            }

            .blog-card:hover {
                transform: translateY(-4px);
                box-shadow: 0 6px 18px rgba(0, 0, 0, 0.4);
            }

            .blog-card-link {
                text-decoration: none;
                color: inherit;
                display: block;
            }

            .blog-header {
                display: flex;
                align-items: center;
                margin-bottom: 12px;
            }

            .blog-user {
                width: 48px;
                height: 48px;
                border-radius: 50%;
                margin-right: 12px;
            }

            .blog-user-info {
                display: flex;
                flex-direction: column;
                justify-content: center;
            }

            .blog-name {
                font-weight: 600;
                color: #e2e8f0;
            }

            .blog-date {
                font-size: 0.85rem;
                color: #94a3b8;
            }

            .blog-content {
                margin-top: 10px;
            }

            .blog-title {
                font-weight: 700;
                font-size: 1.2rem;
                margin: 10px 0 6px;
                color: #f8fafc;
            }

            .blog-description {
                font-size: 1rem;
                color: #cbd5e1;
                line-height: 1.5;
            }

            .blog-image {
                width: 100%;
                border-radius: 10px;
                height: auto;
                max-height: 400px;
                object-fit: cover;
                margin-bottom: 10px;
            }

            /* Button if you add "Xem thêm" later */
            .blog-button {
                background-color: #334155;
                color: #fff;
                padding: 10px 20px;
                border-radius: 8px;
                border: none;
                cursor: pointer;
                font-weight: 500;
                transition: background-color 0.2s;
            }

            .blog-button:hover {
                background-color: #475569;
            }
        </style>

    </head>
    <body>
        <div align="center">
            <c:forEach var="blog" items="${blogs}">
                <div class="blog-card col-8">
                    <a href="blog?id=${blog.id}" class="blog-card-link">
                        <!--<div class="blog-header">
                                                    <img class="blog-user" src="https://i.pravatar.cc/48" alt="User Avatar"/>
                                                    <div class="blog-user-info">
                                                        <div class="blog-name">Name</div>
                                                    </div>
                                                </div>-->
                        <div class="blog-content">
                            <img class="blog-image" src="https://picsum.photos/1024/768" alt="Post Image"/>
                            <div class="blog-title">${blog.title}</div>
                            <p class="blog-description">${blog.content}</p>
                            <div style="display: flex;justify-content: flex-end">
                                <div class="blog-date">${blog.createdAt}</div>
                            </div>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </body>

</html>
