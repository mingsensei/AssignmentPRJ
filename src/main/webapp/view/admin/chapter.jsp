<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chapter Management</title>
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
                    <h1 class="h3 mb-0 text-gray-800">Chương học - Khóa học: ${courseName}</h1>
                    <a href="${pageContext.request.contextPath}/admin/course" class="btn btn-sm btn-secondary">
                        <i class="fas fa-arrow-left"></i> Quay lại danh sách khóa học
                    </a>
                </div>

                <!-- Add Chapter Button -->
                <div class="mb-3">
                    <button class="btn btn-success" data-toggle="modal" data-target="#addChapterModal">
                        <i class="fas fa-plus"></i> Thêm chương mới
                    </button>
                </div>

                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Danh sách chương học</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="chapterTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Thứ tự</th>
                                    <th>Tên chương</th>
                                    <th>Hành động</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="chapter" items="${chapters}">
                                    <tr>
                                        <td>${chapter.id}</td>
                                        <td>
                                            <span class="badge badge-info">${chapter.orderIndex}</span>
                                        </td>
                                        <td>${chapter.title}</td>
                                        <td>
                                            <!-- View -->
                                            <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#viewModal${chapter.id}">
                                                <i class="fas fa-eye"></i> Xem
                                            </button>
                                            <!-- Edit -->
                                            <button class="btn btn-sm btn-primary" data-toggle="modal" data-target="#editModal${chapter.id}">
                                                <i class="fas fa-edit"></i> Sửa
                                            </button>
                                            <!-- Delete -->
                                            <form action="${pageContext.request.contextPath}/admin/chapter/delete" method="post" style="display:inline;" onsubmit="return confirm('Xác nhận xóa chương này?');">
                                                <input type="hidden" name="chapterId" value="${chapter.id}" />
                                                <input type="hidden" name="courseId" value="${courseId}" />
                                                <button type="submit" class="btn btn-sm btn-danger">
                                                    <i class="fas fa-trash"></i> Xóa
                                                </button>
                                            </form>
                                            <!-- Manage Lessons -->
                                            <a href="${pageContext.request.contextPath}/admin/lesson?chapterId=${chapterId}" class="btn btn-sm btn-warning">
                                                <i class="fas fa-book"></i> Bài học
                                            </a>
                                        </td>
                                    </tr>

                                    <!-- View Modal -->
                                    <div class="modal fade" id="viewModal${chapter.id}" tabindex="-1" role="dialog">
                                        <div class="modal-dialog modal-lg" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title">Chi tiết chương #${chapter.id}</h5>
                                                    <button type="button" class="close" data-dismiss="modal">
                                                        <span>&times;</span>
                                                    </button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <p><strong>ID:</strong> ${chapter.id}</p>
                                                            <p><strong>Thứ tự:</strong> ${chapter.orderIndex}</p>
                                                            <p><strong>Tên chương:</strong> ${chapter.title}</p>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <p><strong>Khóa học ID:</strong> ${chapter.courseId}</p>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Edit Modal -->
                                    <div class="modal fade" id="editModal${chapter.id}" tabindex="-1" role="dialog">
                                        <div class="modal-dialog modal-lg" role="document">
                                            <div class="modal-content">
                                                <form method="post" action="${pageContext.request.contextPath}/admin/chapter/update">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">Sửa chương #${chapter.id}</h5>
                                                        <button type="button" class="close" data-dismiss="modal">
                                                            <span>&times;</span>
                                                        </button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <input type="hidden" name="chapterId" value="${chapter.id}" />
                                                        <input type="hidden" name="courseId" value="${courseId}" />

                                                        <div class="row">
                                                            <div class="col-md-6">
                                                                <div class="form-group">
                                                                    <label for="orderIndex${chapter.id}">Thứ tự <span class="text-danger">*</span></label>
                                                                    <input type="number" id="orderIndex${chapter.id}" name="orderIndex" class="form-control" value="${chapter.orderIndex}" required min="1" />
                                                                </div>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <div class="form-group">
                                                                    <label for="title${chapter.id}">Tên chương <span class="text-danger">*</span></label>
                                                                    <input type="text" id="title${chapter.id}" name="title" class="form-control" value="${chapter.title}" required maxlength="255" />
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="submit" class="btn btn-primary">
                                                            <i class="fas fa-save"></i> Lưu thay đổi
                                                        </button>
                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                                                    </div>
                                                </form>
                                            </div>
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

<!-- Add Chapter Modal -->
<div class="modal fade" id="addChapterModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/admin/chapter/add">
                <div class="modal-header">
                    <h5 class="modal-title">Thêm chương mới</h5>
                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="courseId" value="${courseId}" />

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="addOrderIndex">Thứ tự <span class="text-danger">*</span></label>
                                <input type="number" id="addOrderIndex" name="orderIndex" class="form-control" required min="1" />
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="addTitle">Tên chương <span class="text-danger">*</span></label>
                                <input type="text" id="addTitle" name="title" class="form-control" required maxlength="255" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">
                        <i class="fas fa-plus"></i> Thêm chương
                    </button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/demo/datatables-demo.js"></script>

<script>
    $(document).ready(function() {
        // Initialize DataTable for chapters
        $('#chapterTable').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json"
            },
            "pageLength": 25,
            "order": [[ 1, "asc" ]] // Sort by order index
        });

        // Auto-suggest next order index
        var maxOrderIndex = 0;
        <c:forEach var="chapter" items="${chapters}">
        if (${chapter.orderIndex} > maxOrderIndex) {
            maxOrderIndex = ${chapter.orderIndex};
        }
        </c:forEach>

        $('#addOrderIndex').val(maxOrderIndex + 1);
    });
</script>
</body>
</html>