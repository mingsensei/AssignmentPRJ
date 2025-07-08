<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Blog post</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/css/markdownstyle.css" />
        <style>
            body {
                background-color: #f8fafc; /* light background */
                font-family: 'Segoe UI', sans-serif;
                color: #1e293b;
                margin: 0;
            }

            .blog-card {
                background-color: #ffffff;
                border: 1px solid #e2e8f0;
                border-radius: 12px;
                padding: 16px;
                margin-bottom: 20px;
                max-width: 700px;
                text-align: left;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
                transition: transform 0.2s, box-shadow 0.2s;
            }

            .blog-card:hover {
                transform: translateY(-4px);
                box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
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
                color: #1e293b;
            }

            .blog-date {
                font-size: 0.85rem;
                color: #64748b;
            }

            .blog-content {
                margin-top: 10px;
            }

            .blog-title {
                font-weight: 700;
                font-size: 1.2rem;
                margin: 10px 0 6px;
                color: #0f172a;
            }

            .blog-description {
                font-size: 1rem;
                color: #334155;
                line-height: 1.6;
            }

            .blog-image {
                width: 100%;
                border-radius: 10px;
                height: auto;
                max-height: 400px;
                object-fit: cover;
                margin-bottom: 10px;
            }

            .blog-button {
                background-color: #e2e8f0;
                color: #1e293b;
                padding: 10px 20px;
                border-radius: 8px;
                border: none;
                cursor: pointer;
                font-weight: 500;
                transition: background-color 0.2s;
            }

            .blog-button:hover {
                background-color: #cbd5e1;
            }
            .fab-button {
                position: fixed;
                bottom: 24px;
                right: 24px;
                width: 56px;
                height: 56px;
                background-color: #0f172a;
                color: white;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 2rem;
                text-decoration: none;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
                transition: background-color 0.2s ease, transform 0.2s ease;
                z-index: 100;
            }

            .fab-button:hover {
                background-color: #1e293b;
                transform: scale(1.1);
            }

        </style>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <a href="blog?action=create" class="fab-button" title="Create a new blog post">+</a>
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
                            <div class="blog-title">${blog.title}</div>
                            <div class="markdown-style" data-raw="${fn:escapeXml(blog.content)}"></div>
                            <div style="display: flex;justify-content: flex-end">
                                <div class="blog-date">${blog.createdAt}</div>
                            </div>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
        <%@ include file="footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
        <script>
            document.querySelectorAll('.markdown-style').forEach(el => {
                const raw = el.dataset.raw;
                el.innerHTML = marked.parse(raw);
            });
        </script>
    </body>

</html>
