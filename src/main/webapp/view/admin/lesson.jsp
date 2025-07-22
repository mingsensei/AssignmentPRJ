<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lesson Management</title>
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,300,400,700" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">
</head>
<body id="page-top">
<div id="wrapper">
    <jsp:include page="sidebar.jsp" />

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <jsp:include page="topbar.jsp" />

            <div class="container-fluid">
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Bài học - Chương: ${chapterTitle}</h1>
                    <a href="${pageContext.request.contextPath}/admin/chapter?courseId=${courseId}" class="btn btn-sm btn-secondary">
                        <i class="fas fa-arrow-left"></i> Quay lại danh sách chương học
                    </a>
                </div>

                <div class="mb-3">
                    <button class="btn btn-success" data-toggle="modal" data-target="#addLessonModal">
                        <i class="fas fa-plus"></i> Thêm bài học mới
                    </button>
                </div>

                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Danh sách bài học</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="lessonTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Thứ tự</th>
                                    <th>Tên bài học</th>
                                    <th>Hành động</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="lesson" items="${lessons}">
                                    <tr>
                                        <td>${lesson.id}</td>
                                        <td><span class="badge badge-info">${lesson.orderIndex}</span></td>
                                        <td>${lesson.title}</td>
                                        <td>
                                            <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#viewModal${lesson.id}"><i class="fas fa-eye"></i> Xem</button>
                                            <button class="btn btn-sm btn-primary" data-toggle="modal" data-target="#editModal${lesson.id}"><i class="fas fa-edit"></i> Sửa</button>
                                            <form action="${pageContext.request.contextPath}/admin/lesson/delete" method="post" style="display:inline;" onsubmit="return confirm('Xác nhận xóa bài học này?');">
                                                <input type="hidden" name="lessonId" value="${lesson.id}" />
                                                <input type="hidden" name="chapterId" value="${chapterId}" />
                                                <button type="submit" class="btn btn-sm btn-danger"><i class="fas fa-trash"></i> Xóa</button>
                                            </form>
                                        </td>
                                    </tr>

                                    <div class="modal fade" id="viewModal${lesson.id}" tabindex="-1" role="dialog">
                                        <div class="modal-dialog modal-lg" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title">Chi tiết: ${lesson.title}</h5>
                                                    <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                                                </div>
                                                <div class="modal-body">
                                                    <p><strong>ID:</strong> ${lesson.id}</p>
                                                    <p><strong>Thứ tự:</strong> ${lesson.orderIndex}</p>
                                                    <p><strong>Video URL:</strong> <a href="${lesson.videoUrl}" target="_blank">${lesson.videoUrl}</a></p>
                                                    <hr>
                                                    <p><strong>Mô tả:</strong></p>
                                                    <div style="white-space: pre-wrap; background-color: #f8f9fa; padding: 15px; border-radius: 5px; max-height: 300px; overflow-y: auto;">${lesson.description}</div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="modal fade" id="editModal${lesson.id}" tabindex="-1" role="dialog">
                                        <div class="modal-dialog modal-lg" role="document">
                                            <form method="post" action="${pageContext.request.contextPath}/admin/lesson/update">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">Sửa bài học #${lesson.id}</h5>
                                                        <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <input type="hidden" name="lessonId" value="${lesson.id}" />
                                                        <input type="hidden" name="chapterId" value="${chapterId}" />
                                                        <div class="form-group">
                                                            <label>Tên bài học <span class="text-danger">*</span></label>
                                                            <input type="text" name="title" class="form-control" value="${lesson.title}" required maxlength="100" />
                                                        </div>
                                                        <div class="form-group">
                                                            <label>Thứ tự <span class="text-danger">*</span></label>
                                                            <input type="number" name="orderIndex" class="form-control" value="${lesson.orderIndex}" required min="1" />
                                                        </div>
                                                        <div class="form-group">
                                                            <label>Video URL</label>
                                                            <input type="text" name="videoUrl" class="form-control" value="${lesson.videoUrl}" />
                                                        </div>
                                                        <div class="form-group">
                                                            <label>Mô tả</label>
                                                            <textarea name="description" class="form-control" rows="8">${lesson.description}</textarea>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Lưu thay đổi</button>
                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" />
    </div>
</div>

<div class="modal fade" id="addLessonModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <form method="post" action="${pageContext.request.contextPath}/admin/lesson/add">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Thêm bài học mới</h5>
                    <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="chapterId" value="${chapterId}" />
                    <div class="form-group">
                        <label>Tên bài học <span class="text-danger">*</span></label>
                        <input type="text" name="title" class="form-control" required maxlength="100" />
                    </div>
                    <div class="form-group">
                        <label>Thứ tự <span class="text-danger">*</span></label>
                        <input type="number" id="addOrderIndex" name="orderIndex" class="form-control" required min="1" />
                    </div>
                    <div class="form-group">
                        <label>Video URL</label>
                        <input type="text" name="videoUrl" class="form-control" />
                    </div>
                    <div class="form-group">
                        <label>Mô tả</label>
                        <textarea name="description" class="form-control" rows="8"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success"><i class="fas fa-plus"></i> Thêm bài học</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.js"></script>
<script>
    $(document).ready(function() {
        $('#lessonTable').DataTable({
            "language": { "url": "https://cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json" },
            "pageLength": 10,
            "order": [[ 1, "asc" ]],
            "columnDefs": [ { "orderable": false, "targets": 3 } ]
        });

        var maxOrderIndex = 0;
        <c:forEach var="lesson" items="${lessons}">
        if (${lesson.orderIndex} > maxOrderIndex) { maxOrderIndex = ${lesson.orderIndex}; }
        </c:forEach>
        $('#addOrderIndex').val(maxOrderIndex + 1);
    });
</script>
</body>
</html>