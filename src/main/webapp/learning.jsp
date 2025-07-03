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
                        <span class="status-icon status-none"></span>
                    </div>
                    <c:set var="chapterIdStr" value="${chapter.id}" />
                    <div class="collapse" id="chapter${chapter.id}">
                    <c:forEach var="ls" items="${allLessons}">
                            <c:if test="${ls.chapterId == chapter.id}">
                                <div class="lesson-link${ls.id == param.lessonid ? ' viewed' : ''}">
                                    <a href="learning?courseid=${course.id}&chapterid=${chapter.id}&lessonid=${ls.id}" style="text-decoration:none;color:inherit;display:block;width:100%">
                                        ${chapStatus.index + 1}.${ls.orderIndex} ${ls.title}
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
                        <video id="lesson-video" controls>
                            <source src="${lesson.videoUrl}" type="video/mp4" />
                            Trình duyệt không hỗ trợ video.
                        </video>
                    </div>
                    <div id="lesson-description" class="mt-4">
                        <h5 class="text-secondary">Mô tả bài học</h5>
                        <p>${lesson.description}</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <h3 id="lesson-title" class="text-primary">Chào mừng bạn!</h3>
                    <div class="video-wrapper mt-3">
                        <video id="lesson-video" controls>
                            <source src="" type="video/mp4" />
                            Trình duyệt không hỗ trợ video.
                        </video>
                    </div>
                    <div id="lesson-description" class="mt-4">
                        <h5 class="text-secondary">Mô tả bài học</h5>
                        <p>Hãy chọn một bài học từ danh sách bên trái để xem chi tiết.</p>
                    </div>
                </c:otherwise>
            </c:choose>
            <div class="mt-4 mb-4">
                <button class="btn btn-success px-4 py-2">Làm quiz ôn tập</button>
            </div>
            <div class="comment-box">
                <h6 class="text-dark">Bình luận</h6>
                <textarea class="form-control mb-2" rows="3" placeholder="Nhập bình luận..."></textarea>
                <button class="btn btn-primary btn-sm mb-3">Gửi</button>
                <div class="comment">
                    <strong>User123:</strong>
                    <p>Bài giảng rất dễ hiểu!</p>
                    <a href="#" class="text-decoration-none small text-primary" onclick="this.nextElementSibling.classList.toggle('d-none'); return false;">Phản hồi</a>
                    <div class="reply-box d-none">
                        <textarea class="form-control mb-2" rows="2" placeholder="Nhập phản hồi..."></textarea>
                        <button class="btn btn-outline-primary btn-sm">Gửi phản hồi</button>
                    </div>
                </div>
            </div>
        </main>
    </div>
    <%@ include file="footer.jsp" %>
</div>
<script>
    function toggleSidebar() {
        const sidebar = document.getElementById('sidebar');
        const icon = document.getElementById('toggle-icon');
        sidebar.classList.toggle('collapsed');
        icon.textContent = sidebar.classList.contains('collapsed') ? '»' : '«';
    }
        function toggleChapter(chapterId) {
        const content = document.getElementById("chapter" + chapterId);
        if (content.classList.contains("show")) {
        bootstrap.Collapse.getInstance(content).hide();
    } else {
        if (!bootstrap.Collapse.getInstance(content)) {
        new bootstrap.Collapse(content);
    } else {
        bootstrap.Collapse.getInstance(content).show();
    }
    }
    }

</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>

</html>
