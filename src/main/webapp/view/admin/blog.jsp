<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Blog Management</title>
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
                            <h1 class="h3 mb-0 text-gray-800">Quản lý bài viết</h1>
                            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-sm btn-secondary">
                                <i class="fas fa-arrow-left"></i> Về trang quản trị
                            </a>
                        </div>

                        <div class="mb-3">
                            <a href="${pageContext.request.contextPath}/blog?action=create" class="btn btn-success">
                                <i class="fas fa-plus"></i> Thêm blog mới
                            </a>
                        </div>

                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Danh sách blog</h6>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered" id="blogTable" width="100%" cellspacing="0">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Tiêu đề</th>
                                                <th>Ngày tạo</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                            <c:forEach var="blog" items="${blogs}">

                                                <tr>
                                                    <td>${blog.id}</td>
                                                    <td>${blog.title}</td>
                                                    <td>${blog.createdAt}</td>
                                                    <td>
                                                        <!-- View -->
                                                        <a href="${pageContext.request.contextPath}/blog?id=${blog.id}" class="btn btn-sm btn-info">
                                                            <i class="fas fa-eye"></i> Xem
                                                        </a>
                                                        <!-- Edit -->
                                                        <a href="${pageContext.request.contextPath}/blog?action=edit&id=${blog.id}" class="btn btn-sm btn-primary">
                                                            <i class="fas fa-edit"></i> Sửa
                                                        </a>
                                                        <!-- Delete -->
                                                        <form action="${pageContext.request.contextPath}/admin/blog/delete" method="post" style="display:inline;" onsubmit="return confirm('Xác nhận xóa blog này?');">
                                                            <input type="hidden" name="id" value="${blog.id}" />
                                                            <button type="submit" class="btn btn-sm btn-danger">
                                                                <i class="fas fa-trash"></i> Xóa
                                                            </button>
                                                        </form>
                                                    </td>
                                                </tr>
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
        <!-- Scripts -->
        <script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/datatables/jquery.dataTables.min.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.js"></script>

        <script>
                                                            $(document).ready(function () {
                                                                $('#blogTable').DataTable({
                                                                    "language": {
                                                                        "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json"
                                                                    },
                                                                    "pageLength": 10,
                                                                    "order": [[2, "desc"]]
                                                                });
                                                            });
        </script>
    </body>
</html>
