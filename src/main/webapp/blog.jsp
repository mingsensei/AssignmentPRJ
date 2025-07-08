<%@ page import="org.example.rf.model.User"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
    User currentUser = (User) session.getAttribute("user");
    request.setAttribute("currentUser", currentUser);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>${blog.title}</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/css/markdownstyle.css" />
        <style>
            body {
                margin: 0;
                font-family: 'Segoe UI', sans-serif;
                background-color: #f8fafc;
                color: #1e293b;
            }

            .page-wrapper {
                display: flex;
                gap: 2rem;
                padding: 2rem;
            }

            #toc {
                min-width: 200px;
                position: sticky;
                top: 1rem;
                align-self: flex-start;
                background-color: #f1f5f9;
                padding: 1rem;
                border-radius: 8px;
                font-size: 0.9rem;
                color: #334155;
                max-height: 90vh;
                overflow-y: auto;
            }

            #toc h3 {
                margin-top: 0;
                font-size: 1rem;
                font-weight: bold;
                color: #1e293b;
            }

            #toc ul {
                list-style-type: none;
                padding-left: 0;
            }

            #toc li {
                margin: 0.4rem 0;
            }

            #toc a {
                text-decoration: none;
                color: #2563eb;
            }

            #toc a:hover {
                text-decoration: underline;
            }

            .blog-container {
                max-width: 800px;
                padding: 0 1rem;
            }

            .blog-breadcrumb {
                font-size: 0.9rem;
                color: #6b7280;
                margin-bottom: 1rem;
            }

            .blog-breadcrumb-link {
                color: inherit;
                text-decoration: none;
                transition: color 0.2s ease;
            }

            .blog-breadcrumb-link:hover {
                color: #2563eb;
                text-decoration: underline;
            }

            .blog-title {
                font-size: 2.5rem;
                font-weight: 700;
                margin-bottom: 1rem;
                color: #0f172a;
            }

            .blog-meta {
                display: flex;
                align-items: center;
                gap: 1rem;
                margin-bottom: 2rem;
                color: #475569;
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
                color: #1e293b;
            }

            .blog-excerpt {
                background-color: #f1f5f9;
                padding: 1rem;
                font-style: italic;
                border-radius: 8px;
                margin-bottom: 2rem;
                color: #334155;
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
                color: #334155;
            }

            .blog-tag {
                background-color: #e2e8f0;
                color: #1e293b;
                padding: 0.25rem 0.75rem;
                border-radius: 9999px;
                font-size: 0.85rem;
                margin-bottom: 0.5rem;
                display: inline-block;
            }
            .comment-container {
                display: flex;
                justify-content: center;
                padding: 2rem 1rem;
                background-color: #fff;
            }

            .comment-box {
                width: 100%;
                max-width: 800px;
            }

            .comment-item {
                margin-bottom: 1rem;
                padding: 1rem;
                background: #f1f5f9;
                border-radius: 8px;
            }

            .comment-item .username {
                font-weight: bold;
                margin-bottom: 0.5rem;
            }

            .comment-item .timestamp {
                font-size: 0.85rem;
                color: #6b7280;
                margin-top: 0.5rem;
            }

            textarea#commentInput {
                width: 100%;
                font-family: monospace;
                padding: 0.5rem;
                margin-top: 1rem;
                border: 1px solid #ccc;
                border-radius: 4px;
                resize: vertical;
            }

            #commentPreview {
                background: #fff;
                padding: 0.5rem;
                border: 1px solid #ccc;
                border-radius: 4px;
                margin-top: 0.5rem;
            }

            button[type="submit"] {
                margin-top: 1rem;
                background-color: #2563eb;
                color: white;
                border: none;
                padding: 0.5rem 1rem;
                border-radius: 6px;
                cursor: pointer;
            }

        </style>
    </head>
    <body>
        <%@ include file="header.jsp" %>

        <div class="page-wrapper">
            <!-- TOC Sidebar -->
            <aside id="toc" class="toc"></aside>

            <!-- Blog Content -->
            <div class="blog-container">
                <div class="blog-breadcrumb">
                    <a href="index.jsp" class="blog-breadcrumb-link">🏠</a> &nbsp;&gt;&nbsp;
                    <a href="blog" class="blog-breadcrumb-link">Post</a> &nbsp;&gt;
                    ${blog.title}
                </div>
                <!-- Show Edit button if current user is connected to this blog -->
                <c:if test="${not empty currentUser}">
                    <c:forEach var="blogUser" items="${blogUsers}">
                        <c:if test="${blogUser.user.id == currentUser.id}">
                            <form action="blog" method="get" style="margin-bottom: 1rem;">
                                <input type="hidden" name="id" value="${blog.id}" />
                                <input type="hidden" name="action" value="edit" />
                                <button type="submit" style="
                                        background-color: #10b981;
                                        color: white;
                                        border: none;
                                        padding: 0.5rem 1rem;
                                        border-radius: 6px;
                                        cursor: pointer;
                                        ">✏️ Edit Blog</button>
                            </form>
                            <c:remove var="blogUser"/>
                        </c:if>
                    </c:forEach>
                </c:if>
                <div class="blog-title">${blog.title}</div>

                <div class="blog-meta">
                    <img src="https://i.pravatar.cc/48" class="author-avatar" alt="Author"/>
                    <c:forEach var="blogUser" items="${blogUsers}">
                        <div class="author-info">
                            <div class="name">${blogUser.user.userName}</div>
                            <div class="role">${blogUser.blogRole}</div>
                        </div>
                    </c:forEach>
                    <div>${blog.createdAt}</div>
                </div>

                <div class="markdown-style" data-raw="${fn:escapeXml(blog.content)}"></div>
            </div>


        </div>
        <!-- Comment Section -->
        <jsp:include page="/Comment">
            <jsp:param name="type" value="blog" />
            <jsp:param name="id" value="${blog.id}" />
        </jsp:include>


        <%@ include file="footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
        <script>
            document.querySelectorAll('.markdown-style').forEach(el => {
                const raw = el.dataset.raw;
                const html = marked.parse(raw);
                const tempDiv = document.createElement('div');
                tempDiv.innerHTML = html;

                // Assign headings IDs BEFORE inserting into DOM
                function slugify(text) {
                    return text
                            .toLowerCase()
                            .trim()
                            .replace(/[^\w\s-]/g, '')  // remove non-word characters
                            .replace(/\s+/g, '-')      // replace spaces with hyphens
                            .replace(/-+/g, '-');      // collapse multiple hyphens
                }

                // Assign heading IDs before inserting into DOM
                const usedIds = new Set();
                tempDiv.querySelectorAll('h1, h2, h3').forEach(heading => {
                    let baseId = slugify(heading.textContent);
                    let uniqueId = baseId;
                    let suffix = 1;
                    while (usedIds.has(uniqueId)) {
                        uniqueId = baseId + "-" + (suffix++);
                    }
                    usedIds.add(uniqueId);
                    heading.id = uniqueId;
                });

                // Now insert the parsed content into the page
                el.innerHTML = tempDiv.innerHTML;

                // Build the TOC from the now-inserted headings
                const toc = document.getElementById('toc');
                toc.innerHTML = '<h3>Contents</h3><ul>';

                count = 0;
                el.querySelectorAll('h1, h2, h3').forEach(heading => {
                    const tag = heading.tagName.toLowerCase();
                    const text = heading.textContent;
                    const anchor = heading.id;

                    let margin = '0';
                    if (tag === 'h2')
                        margin = '1rem';
                    if (tag === 'h3')
                        margin = '2rem';

                    toc.innerHTML +=
                            '<li style="margin-left: ' + margin + '"><a href="#' + anchor + '">' + text + '</a></li>';
                });

                toc.innerHTML += '</ul>';
            });

            function handleCommentSubmit(event) {
                const content = document.getElementById('commentInput').value.trim();
                if (!content) {
                    alert("Comment cannot be empty.");
                    event.preventDefault();
                    return false;
                }
                return true;
            }
        </script>
    </body>
</html>
