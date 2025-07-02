<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Settings - Category Management</title>
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
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Cài đặt hệ thống</h1>
                </div>

                <!-- Alert Messages -->
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle"></i> ${successMessage}
                        <button type="button" class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                    </div>
                </c:if>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                        <button type="button" class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                    </div>
                </c:if>

                <!-- Category Management Section -->
                <div class="row">
                    <div class="col-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    <i class="fas fa-tags"></i> Quản lý danh mục khóa học
                                </h6>
                                <button class="btn btn-primary btn-sm" data-toggle="modal" data-target="#addCategoryModal">
                                    <i class="fas fa-plus"></i> Thêm danh mục
                                </button>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered" id="categoryTable" width="100%" cellspacing="0">
                                        <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Tên danh mục</th>
                                            <th>Mô tả</th>
                                            <th>Hành động</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="category" items="${categories}">
                                            <tr>
                                                <td>${category.id}</td>
                                                <td>${category.name}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${category.description.length() > 100}">
                                                            ${category.description.substring(0, 100)}...
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${category.description}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <!-- View -->
                                                    <button class="btn btn-sm btn-info" data-toggle="modal"
                                                            data-target="#viewCategoryModal${category.id}">
                                                        <i class="fas fa-eye"></i>
                                                    </button>
                                                    <!-- Edit -->
                                                    <button class="btn btn-sm btn-warning" data-toggle="modal"
                                                            data-target="#editCategoryModal${category.id}">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <!-- Delete -->
                                                    <form action="${pageContext.request.contextPath}/admin/category/delete"
                                                          method="get" style="display:inline;"
                                                          onsubmit="return confirm('Xác nhận xóa danh mục này? Lưu ý: Không thể xóa danh mục đang được sử dụng.');">
                                                        <input type="hidden" name="categoryId" value="${category.id}"/>
                                                        <button type="submit" class="btn btn-sm btn-danger">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>

                                            <!-- View Category Modal -->
                                            <div class="modal fade" id="viewCategoryModal${category.id}" tabindex="-1" role="dialog">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">
                                                                <i class="fas fa-tag"></i> Chi tiết danh mục #${category.id}
                                                            </h5>
                                                            <button type="button" class="close" data-dismiss="modal">
                                                                <span>&times;</span>
                                                            </button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <div class="row">
                                                                <div class="col-md-4">
                                                                    <strong>ID:</strong>
                                                                </div>
                                                                <div class="col-md-8">
                                                                        ${category.id}
                                                                </div>
                                                            </div>
                                                            <hr>
                                                            <div class="row">
                                                                <div class="col-md-4">
                                                                    <strong>Tên danh mục:</strong>
                                                                </div>
                                                                <div class="col-md-8">
                                                                        ${category.name}
                                                                </div>
                                                            </div>
                                                            <hr>
                                                            <div class="row">
                                                                <div class="col-md-4">
                                                                    <strong>Mô tả:</strong>
                                                                </div>
                                                                <div class="col-md-8">
                                                                    <c:choose>
                                                                        <c:when test="${empty category.description}">
                                                                            <span class="text-muted">Không có mô tả</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            ${category.description}
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                                                Đóng
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <!-- Edit Category Modal -->
                                            <div class="modal fade" id="editCategoryModal${category.id}" tabindex="-1" role="dialog">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">
                                                        <form method="post" action="${pageContext.request.contextPath}/admin/category/update">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title">
                                                                    <i class="fas fa-edit"></i> Sửa danh mục #${category.id}
                                                                </h5>
                                                                <button type="button" class="close" data-dismiss="modal">
                                                                    <span>&times;</span>
                                                                </button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <input type="hidden" name="categoryId" value="${category.id}"/>

                                                                <div class="form-group">
                                                                    <label for="editName${category.id}">
                                                                        Tên danh mục <span class="text-danger">*</span>
                                                                    </label>
                                                                    <input type="text" id="editName${category.id}" name="name"
                                                                           class="form-control" value="${category.name}"
                                                                           required maxlength="100"/>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label for="editDescription${category.id}">Mô tả</label>
                                                                    <textarea id="editDescription${category.id}" name="description"
                                                                              class="form-control" rows="3"
                                                                              maxlength="255">${category.description}</textarea>
                                                                    <small class="form-text text-muted">Tối đa 255 ký tự</small>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="submit" class="btn btn-primary">
                                                                    <i class="fas fa-save"></i> Lưu thay đổi
                                                                </button>
                                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                                                    Hủy
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

                <!-- Additional Settings Sections can be added here -->
                <!-- For example: System Configuration, User Roles, etc. -->

            </div>
        </div>

        <jsp:include page="footer.jsp"/>
    </div>
</div>

<!-- Add Category Modal -->
<div class="modal fade" id="addCategoryModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/admin/category">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-plus"></i> Thêm danh mục mới
                    </h5>
                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="addCategoryName">
                            Tên danh mục <span class="text-danger">*</span>
                        </label>
                        <input type="text" id="addCategoryName" name="name" class="form-control"
                               required maxlength="100" placeholder="Nhập tên danh mục"/>
                    </div>

                    <div class="form-group">
                        <label for="addCategoryDescription">Mô tả</label>
                        <textarea id="addCategoryDescription" name="description" class="form-control"
                                  rows="3" maxlength="255" placeholder="Nhập mô tả cho danh mục (tùy chọn)"></textarea>
                        <small class="form-text text-muted">Tối đa 255 ký tự</small>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">
                        <i class="fas fa-plus"></i> Thêm danh mục
                    </button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">
                        Hủy
                    </button>
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

<script>
    $(document).ready(function () {
        // Initialize DataTable for categories
        $('#categoryTable').DataTable({
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

        // Auto-hide alerts after 5 seconds
        setTimeout(function() {
            $('.alert').fadeOut('slow');
        }, 5000);
    });
</script>

</body>
</html>