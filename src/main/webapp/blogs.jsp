<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Blog Posts</title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/css/markdownstyle.css" />

        <style>
            body {
                background-color: #f8f9fa;
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
                z-index: 100;
            }

            .fab-button:hover {
                background-color: #1e293b;
                transform: scale(1.1);
            }

            .carousel-container {
                width: 100%;
                max-width: 960px;
                margin: 20px auto;
                background-color: white;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                overflow: hidden;
            }

            .carousel-track {
                display: flex;
                width: 400%;
                transition: transform 0.6s ease-in-out;
            }

            .carousel-card {
                flex: 0 0 25%;
                box-sizing: border-box;
                padding: 20px;
                text-align: center;
            }

            .carousel-card img {
                width: 100%;
                max-height: 240px;
                object-fit: cover;
                border-radius: 8px;
            }

            .carousel-title {
                font-size: 1.25rem;
                font-weight: 600;
                color: #0d6efd;
                margin-top: 16px;
            }

            .carousel-description {
                font-size: 0.95rem;
                color: #555;
                margin-top: 8px;
            }

            .dots {
                text-align: center;
                margin-top: 10px;
            }

            .dot {
                width: 10px;
                height: 10px;
                display: inline-block;
                background: #ccc;
                border-radius: 50%;
                margin: 0 5px;
                cursor: pointer;
            }

            .dot.active {
                background: #0d6efd;
            }

            .blog-thumbnail {
                max-height: 300px;
                object-fit: cover;
                border-top-left-radius: 0.5rem;
                border-top-right-radius: 0.5rem;
            }

            .blog-card {
                display: none;
            }

            .blog-card:nth-child(-n+8) {
                display: block;
            }
        </style>
    </head>
    <body>
        <%@ include file="header.jsp" %>

        <!-- FAB -->
        <a href="blog?action=create" class="fab-button" title="Create a new blog post">+</a>

        <!-- Most Viewed -->
        <div class="container mt-5">
            <h2 class="text-center fw-bold mb-4">Most Viewed Blogs</h2>
            <div class="carousel-container">
                <div class="carousel-track" id="carouselTrack">
                    <c:forEach var="blog" items="${mostViewed}">
                        <div class="carousel-card">
                            <a href="blog?id=${blog.id}" class="text-decoration-none text-dark">
                                <div class="blog-thumbnail">
                                    <img src="${(blog.thumbnail == null) 
                                            ? 'https://upload.wikimedia.org/wikipedia/commons/7/75/No_image_available.png' 
                                            : blog.thumbnail}" alt="${blog.title}">
                                </div>
                                <div class="carousel-title" style="font-size: 2rem">${blog.title}</div>
                                <div class="carousel-description">
                                    <c:choose>
                                        <c:when test="${not empty blog.description}">
                                            ${blog.description}
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${fn:substring(blog.content, 0, 100)}" />...
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="dots" id="carouselDots"></div>
        </div>

        <!-- Newest Blogs -->
        <div class="container py-5">
            <h2 class="text-center fw-bold mb-4">Newest Blogs</h2>
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4" id="newest-grid">
                <c:forEach var="blog" items="${newestBlogs}" varStatus="loop">
                    <c:if test="${loop.index < 8}">
                        <div class="col blog-card">
                            <div class="card h-100 shadow-sm">
                                <a href="blog?id=${blog.id}" class="text-decoration-none text-dark">
                                    <img src="${(blog.thumbnail==null) 
                                                ? 'https://upload.wikimedia.org/wikipedia/commons/7/75/No_image_available.png' 
                                                : blog.thumbnail}"
                                         class="card-img-top blog-thumbnail" alt="Thumbnail">
                                    <div class="card-body">
                                        <h5 class="card-title">${blog.title}</h5>
                                        <p class="card-text">${blog.description}</p>
                                    </div>
                                </a>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <!-- Expand Button -->
            <c:if test="${fn:length(newestBlogs) > 8}">
                <div class="text-center mt-4">
                    <button class="btn btn-primary px-4 py-2" id="expandNewest">Show More</button>
                </div>
            </c:if>
        </div>

        <%@ include file="footer.jsp" %>

        <!-- Bootstrap + Carousel + Expand -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="<%= request.getContextPath()%>/js/carousel.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>

        <script>
            // Markdown rendering
            document.querySelectorAll('.markdown-style').forEach(el => {
                el.innerHTML = marked.parse(el.dataset.raw);
            });

            // Expand newest section
            document.addEventListener('DOMContentLoaded', function () {
                const expandBtn = document.getElementById('expandNewest');
                if (expandBtn) {
                    expandBtn.addEventListener('click', () => {
                        document.querySelectorAll('.blog-card').forEach(card => card.style.display = 'block');
                        expandBtn.style.display = 'none';
                    });
                }
            });
        </script>
    </body>
</html>
