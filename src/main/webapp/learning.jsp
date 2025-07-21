<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Modern Learning Interface</title>
        <link href="css/learning.css" rel="stylesheet" type="text/css" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    </head>
    <%@ include file="header.jsp" %>
    <script>
        const userId = ${sessionScope.user.id};
    </script>
    <script src="js/learning.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://www.youtube.com/iframe_api"></script>

    <body>

        <div class="toggle-sidebar-btn" onclick="toggleSidebar()">
            <span id="toggle-icon">«</span>
        </div>
        <div class="container-er">
            <div class="wrapper">

                <nav class="sidebar p-3" id="sidebar">
                    <h5 class="mb-4 text-primary">
                        <c:choose>
                            <c:when test="${not empty course}">
                                Khóa Học: ${course.name}
                                <c:if test="${isCompletedCourse}">
                                    <span class="tick-course"></span>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                Chọn khóa học
                            </c:otherwise>
                        </c:choose>
                    </h5>

                    <c:forEach var="chapter" items="${chapters}" varStatus="chapStatus">
                        <div class="chapter mb-3">
                            <div class="chapter-title" onclick="toggleChapter(${chapter.id})">
                                Chương ${chapStatus.index + 1}: ${chapter.title}
                                <c:if test="${completedChapterIds.contains(chapter.id)}">
                                    <span class="tick-chapter">Completed</span>
                                </c:if>
                            </div>

                            <div class="collapse ${chapter.id == param.chapterid ? 'show' : ''}" id="chapter${chapter.id}">
                                <c:forEach var="ls" items="${allLessons}">
                                    <c:if test="${ls.chapterId == chapter.id}">
                                        <div class="lesson-link${ls.id == param.lessonid ? ' viewed' : ''}">
                                            <a href="learning?courseid=${course.id}&chapterid=${chapter.id}&lessonid=${ls.id}"
                                               style="text-decoration:none;color:inherit;display:block;width:100%">
                                                ${chapStatus.index + 1}.${ls.orderIndex} ${ls.title}
                                                <c:if test="${completedLessonIds.contains(ls.id)}">
                                                    <span class="tick-lesson">️</span>
                                                </c:if>
                                            </a>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </nav>

                <main class="content-area" id="contentArea">
                    <c:choose>
                        <c:when test="${not empty lesson}">
                            <h3 id="lesson-title" class="text-primary">${lesson.title}</h3>
                            <div class="video-wrapper mt-3">
                                <!-- KHÔNG để sẵn <iframe> -->
                                <!-- Thay vào đó, tạo 1 <div> rỗng -->
                                <div id="lesson-video"></div>
                            </div>
                            <div id="lesson-description" class="mt-4">
                                <h5 class="text-secondary">Mô tả bài học</h5>
                                <p>${lesson.description}</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <h3 id="lesson-title" class="text-primary">Chào mừng bạn!</h3>
                            <div class="video-wrapper mt-3">
                                <iframe id="lesson-video" width="100%" height="400"
                                        src="https://www.youtube.com/embed/dQw4w9WgXcQ"
                                        frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                        allowfullscreen>
                                </iframe>
                            </div>

                            <div id="lesson-description" class="mt-4">
                                <h5 class="text-secondary">Mô tả bài học</h5>
                                <p>Hãy chọn một bài học từ danh sách bên trái để xem chi tiết.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <div class="mt-4 mb-4">
                        <a href="${pageContext.request.contextPath}/exam/setup/demo" class="btn btn-success px-4 py-2">Làm quiz ôn tập</a>
                    </div>
                    <c:if test="${not empty lesson}">
                        <jsp:include page="/Comment">
                            <jsp:param name="type" value="lesson" />
                            <jsp:param name="id" value="${lesson.id}" />
                        </jsp:include>
                    </c:if>
                </main>
            </div>

            <%@ include file="footer.jsp" %>
        </div>

    </body>

</html>
