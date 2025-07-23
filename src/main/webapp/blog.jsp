<%@ page import="org.example.rf.model.User" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/css/markdownstyle.css" />
        <style>
            body {
                font-family: 'Segoe UI', sans-serif;
                background-color: #f8fafc;
                color: #1e293b;
            }

            #toc {
                background-color: #f1f5f9;
                padding: 1rem;
                border-radius: 8px;
                font-size: 0.9rem;
                color: #334155;
                max-height: 90vh;
                overflow-y: auto;
            }

            #toc h3 {
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

            .markdown-style {
                font-size: 1.1rem;
                line-height: 1.7;
                color: #334155;
            }

            .comment-box textarea {
                font-family: monospace;
            }
            .blog-description {
                background-color: #e0f2fe;
                border-left: 5px solid #0284c7;
                padding: 1rem;
                font-size: 1.1rem;
                border-radius: 6px;
                margin: 1rem 0;
                color: #0f172a;
            }

        </style>
    </head>
    <body>
        <%@ include file="header.jsp" %>

        <div class="container my-4">
            <div class="row">
                <!-- TOC Sidebar -->
                <aside class="col-md-3 mb-4 d-none d-md-block">
                    <div id="toc" class="sticky-top" style="top: 1rem;"></div>
                </aside>

                <!-- Blog Content -->
                <div class="col-md-9">
                    <!-- Breadcrumb -->
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb small">
                            <li class="breadcrumb-item"><a href="index.jsp">🏠</a></li>
                            <li class="breadcrumb-item"><a href="blog">Post</a></li>
                            <li class="breadcrumb-item active" aria-current="page">${blog.title}</li>
                        </ol>
                    </nav>

                    <!-- Edit Button -->
                    <c:if test="${not empty currentUser}">
                        <c:forEach var="blogUser" items="${blogUsers}">
                            <c:if test="${blogUser.user.id == currentUser.id}">
                                <form action="blog" method="get" class="mb-3">
                                    <input type="hidden" name="id" value="${blog.id}" />
                                    <input type="hidden" name="action" value="edit" />
                                    <button type="submit" class="btn btn-success btn-sm">✏️ Edit Blog</button>
                                </form>
                                <c:remove var="blogUser"/>
                            </c:if>
                        </c:forEach>
                    </c:if>

                    <!-- Blog Title -->
                    <h1 class="display-5 fw-bold">${blog.title}</h1>

                    <!-- Blog Meta -->
                    <div class="d-flex align-items-center flex-wrap mb-4 text-muted small gap-3">
                        <c:forEach var="blogUser" items="${blogUsers}">
                            <a href="${pageContext.request.contextPath}/user-info?id=${blogUser.id}">
                                <div class="d-flex align-items-center gap-2">
                                    <img src="${(user.profilePic!=null)?user.profilePic:"https://i.pravatar.cc/150"}" class="rounded-circle" width="40" height="40" alt="Author">
                                    <div>
                                        <div class="fw-semibold text-dark">${blogUser.user.userName}</div>
                                        <div>${blogUser.blogRole}</div>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                    <div class="ms-auto">${blog.createdAt}</div>
                    <img src="${(blog.thumbnail==null)?"https://upload.wikimedia.org/wikipedia/commons/7/75/No_image_available.png":blog.thumbnail}" class="col-md-12" alt="Blog Thumbnail">
                    <c:if test="${not empty blog.description}">
                        <div class="blog-description">
                            ${blog.description}
                        </div>
                    </c:if>

                    <!-- Blog Content -->
                    <div class="markdown-style" data-raw="${fn:escapeXml(blog.content)}"></div>
                    <div id="comment">
                        <jsp:include page="/Comment">
                            <jsp:param name="type" value="blog" />
                            <jsp:param name="id" value="${blog.id}" />
                        </jsp:include>
                    </div>

                </div>
            </div>

            <!-- Comment Section -->

        </div>


        <%@ include file="footer.jsp" %>

        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
        <script>
            document.querySelectorAll('.markdown-style').forEach(el => {
                const raw = el.dataset.raw;
                const html = marked.parse(raw);
                const tempDiv = document.createElement('div');
                tempDiv.innerHTML = html;

                function slugify(text) {
                    return text.toLowerCase().trim().replace(/[^\w\s-]/g, '').replace(/\s+/g, '-').replace(/-+/g, '-');
                }

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

                el.innerHTML = tempDiv.innerHTML;

                const toc = document.getElementById('toc');
                toc.innerHTML = '<h3>Contents</h3><ul>';
                el.querySelectorAll('h1, h2, h3').forEach(heading => {
                    const tag = heading.tagName.toLowerCase();
                    const text = heading.textContent;
                    const anchor = heading.id;
                    let margin = '0';
                    if (tag === 'h2')
                        margin = '1rem';
                    if (tag === 'h3')
                        margin = '2rem';
                    toc.innerHTML += '<li style="margin-left: ' + margin + '"><a href="#' + anchor + '">' + text + '</a></li>';
                });
                toc.innerHTML += `<li style="margin-left: 0"><a href="#comment">Comment</a></li>`;
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
