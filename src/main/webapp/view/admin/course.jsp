<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Course Management</title>
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,300,400,700" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">
</head>
<body id="page-top">
<div id="wrapper">
    <jsp:include page="sidebar.jsp"/>

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <jsp:include page="topbar.jsp"/>

            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Khóa Học</h1>

                <!-- Add Course Button -->
                <div class="mb-3">
                    <button class="btn btn-success" data-toggle="modal" data-target="#addCourseModal">
                        <i class="fas fa-plus"></i> Thêm khóa học mới
                    </button>
                </div>

                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Danh sách khóa học</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="courseTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tên khóa học</th>
                                    <th>Mô tả</th>
                                    <th>Giá</th>
                                    <th>Loại</th>
                                    <th>Học kỳ</th>
                                    <th>Danh mục</th>
                                    <th>Hành động</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="course" items="${courses}">
                                    <tr>
                                        <td>${course.id}</td>
                                        <td>${course.name}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${course.description.length() > 50}">
                                                    ${course.description.substring(0, 50)}...
                                                </c:when>
                                                <c:otherwise>
                                                    ${course.description}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${course.price}" type="currency"
                                                              currencySymbol="VND "/>
                                        </td>
                                        <td>
                                            <span class="badge badge-${course.type == 'online' ? 'info' : 'warning'}">
                                                    ${course.type}
                                            </span>
                                        </td>
                                        <td>${course.semester}</td>
                                        <td>${course.categoryId}</td>
                                        <td>
                                            <!-- View -->
                                            <button class="btn btn-sm btn-info" data-toggle="modal"
                                                    data-target="#viewModal${course.id}">
                                                <i class="fas fa-eye"></i> Xem
                                            </button>
                                            <!-- Edit -->
                                            <button class="btn btn-sm btn-primary" data-toggle="modal"
                                                    data-target="#editModal${course.id}">
                                                <i class="fas fa-edit"></i> Sửa
                                            </button>
                                            <!-- Delete -->
                                            <form action="${pageContext.request.contextPath}/admin/course/delete"
                                                  method="post" style="display:inline;"
                                                  onsubmit="return confirm('Xác nhận xóa khóa học này?');">
                                                <input type="hidden" name="courseId" value="${course.id}"/>
                                                <button type="submit" class="btn btn-sm btn-danger">
                                                    <i class="fas fa-trash"></i> Xóa
                                                </button>
                                            </form>
                                            <a href="${pageContext.request.contextPath}/admin/chapter?courseId=${course.id}"
                                               class="btn btn-sm btn-warning">
                                                <i class="fas fa-book"></i> Chapter
                                            </a>
                                        </td>
                                    </tr>

                                    <!-- View Modal -->
                                    <div class="modal fade" id="viewModal${course.id}" tabindex="-1" role="dialog">
                                        <div class="modal-dialog modal-lg" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title">Chi tiết khóa học #${course.id}</h5>
                                                    <button type="button" class="close" data-dismiss="modal">
                                                        <span>&times;</span>
                                                    </button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <p><strong>ID:</strong> ${course.id}</p>
                                                            <p><strong>Tên khóa học:</strong> ${course.name}</p>
                                                            <p><strong>Giá:</strong> <fmt:formatNumber
                                                                    value="${course.price}" type="currency"
                                                                    currencySymbol="VND "/></p>
                                                            <p><strong>Loại:</strong>
                                                                <span class="badge badge-${course.type == 'online' ? 'info' : 'warning'}">
                                                                        ${course.type}
                                                                </span>
                                                            </p>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <p><strong>Học kỳ:</strong> ${course.semester}</p>
                                                            <p><strong>Danh mục ID:</strong> ${course.categoryId}</p>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-12">
                                                            <p><strong>Mô tả:</strong></p>
                                                            <p class="border p-3 bg-light">${course.description}</p>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary"
                                                            data-dismiss="modal">Đóng
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Edit Modal -->
                                    <div class="modal fade" id="editModal${course.id}" tabindex="-1" role="dialog">
                                        <div class="modal-dialog modal-lg" role="document">
                                            <div class="modal-content">
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/admin/course/update">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">Sửa khóa học #${course.id}</h5>
                                                        <button type="button" class="close" data-dismiss="modal">
                                                            <span>&times;</span>
                                                        </button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <input type="hidden" name="courseId" value="${course.id}"/>

                                                        <div class="row">
                                                            <div class="col-md-6">
                                                                <div class="form-group">
                                                                    <label for="name${course.id}">Tên khóa học <span
                                                                            class="text-danger">*</span></label>
                                                                    <input type="text" id="name${course.id}" name="name"
                                                                           class="form-control" value="${course.name}"
                                                                           required maxlength="100"/>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label for="price${course.id}">Giá <span
                                                                            class="text-danger">*</span></label>
                                                                    <input type="number" id="price${course.id}"
                                                                           name="price" class="form-control"
                                                                           value="${course.price}" required min="0"
                                                                           step="0.01"/>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label for="type${course.id}">Loại khóa học <span
                                                                            class="text-danger">*</span></label>
                                                                    <select id="type${course.id}" name="type"
                                                                            class="form-control" required>
                                                                        <option value="online" ${course.type == 'online' ? 'selected' : ''}>
                                                                            Online
                                                                        </option>
                                                                        <option value="offline" ${course.type == 'offline' ? 'selected' : ''}>
                                                                            Offline
                                                                        </option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <div class="form-group">
                                                                    <label for="semester${course.id}">Học kỳ</label>
                                                                    <input type="number" id="semester${course.id}"
                                                                           name="semester" class="form-control"
                                                                           value="${course.semester}" min="1" max="8"/>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label for="categoryId${course.id}">Danh mục
                                                                        ID</label>
                                                                    <input type="number" id="categoryId${course.id}"
                                                                           name="categoryId" class="form-control"
                                                                           value="${course.categoryId}" min="1"/>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="form-group">
                                                            <label for="description${course.id}">Mô tả</label>
                                                            <textarea id="description${course.id}" name="description"
                                                                      class="form-control" rows="4"
                                                                      maxlength="255">${course.description}</textarea>
                                                            <small class="form-text text-muted">Tối đa 255 ký tự</small>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="submit" class="btn btn-primary">
                                                            <i class="fas fa-save"></i> Lưu thay đổi
                                                        </button>
                                                        <button type="button" class="btn btn-secondary"
                                                                data-dismiss="modal">Hủy
                                                        </button>
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

        <jsp:include page="footer.jsp"/>
    </div>
</div>

<!-- Add Course Modal -->
<div class="modal fade" id="addCourseModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/admin/course/add">
                <div class="modal-header">
                    <h5 class="modal-title">Thêm khóa học mới</h5>
                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="addName">Tên khóa học <span class="text-danger">*</span></label>
                                <input type="text" id="addName" name="name" class="form-control" required
                                       maxlength="100"/>
                            </div>

                            <div class="form-group">
                                <label for="addPrice">Giá <span class="text-danger">*</span></label>
                                <input type="number" id="addPrice" name="price" class="form-control" required min="0"
                                       step="0.01"/>
                            </div>

                            <div class="form-group">
                                <label for="addType">Loại khóa học <span class="text-danger">*</span></label>
                                <select id="addType" name="type" class="form-control" required>
                                    <option value="">-- Chọn loại --</option>
                                    <option value="online">Online</option>
                                    <option value="offline">Offline</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="addSemester">Học kỳ</label>
                                <input type="number" id="addSemester" name="semester" class="form-control" min="1"
                                       max="8"/>
                            </div>

                            <div class="form-group">
                                <label for="addCategoryId">Danh mục ID</label>
                                <input type="number" id="addCategoryId" name="categoryId" class="form-control" min="1"/>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="addDescription">Mô tả</label>
                        <textarea id="addDescription" name="description" class="form-control" rows="4"
                                  maxlength="255"></textarea>
                        <small class="form-text text-muted">Tối đa 255 ký tự</small>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">
                        <i class="fas fa-plus"></i> Thêm khóa học
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
    $(document).ready(function () {
        // Initialize DataTable for courses
        $('#courseTable').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json"
            },
            "pageLength": 25,
            "order": [[0, "desc"]]
        });

        // Character counter for description fields
        $('textarea[name="description"]').on('input', function () {
            var maxLength = 255;
            var currentLength = $(this).val().length;
            var remaining = maxLength - currentLength;

            var counter = $(this).siblings('.char-counter');
            if (counter.length === 0) {
                counter = $('<small class="form-text text-muted char-counter"></small>');
                $(this).after(counter);
            }

            counter.text('Còn lại: ' + remaining + ' ký tự');

            if (remaining < 0) {
                counter.addClass('text-danger').removeClass('text-muted');
            } else {
                counter.addClass('text-muted').removeClass('text-danger');
            }
        });
    });
</script>
</body>
</html>