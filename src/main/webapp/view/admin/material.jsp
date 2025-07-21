<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Material Management</title>
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
                <h1 class="h3 mb-2 text-gray-800">Quản lý Tài liệu</h1>

                <div class="mb-3">
                    <a href="${pageContext.request.contextPath}/admin/material/add" class="btn btn-success">
                        <i class="fas fa-plus"></i> Thêm tài liệu mới
                    </a>
                </div>

                <c:if test="${not empty success}">
                    <div class="alert alert-success" role="alert">${success}</div>
                </c:if>

                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Danh sách tài liệu</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="materialTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tiêu đề</th>
                                    <th>Loại</th>
                                    <th>Link Drive</th>
                                    <th>Thuộc Chapter ID</th>
                                    <th>Hành động</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="material" items="${materials}">
                                    <tr>
                                        <td>${material.id}</td>
                                        <td>${material.title}</td>
                                        <td>
                                            <span class="badge badge-danger">${material.type}</span>
                                        </td>
                                        <td>
                                                <%-- ĐÃ SỬA: Dùng "material.link" --%>
                                            <a href="${material.link}/view" target="_blank" class="btn btn-sm btn-secondary">
                                                <i class="fab fa-google-drive"></i> Xem trên Drive
                                            </a>
                                        </td>
                                            <%-- ĐÃ SỬA: Dùng "material.chapterId" --%>
                                        <td>${material.chapterId}</td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/admin/material/delete"
                                                  method="post" style="display:inline;"
                                                  onsubmit="return confirm('Xác nhận xóa tài liệu này?');">
                                                <input type="hidden" name="materialId" value="${material.id}"/>
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

        <jsp:include page="footer.jsp"/>
    </div>
</div>

<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.js"></script>
<script>
    $(document).ready(function () {
        $('#materialTable').DataTable({
            "language": { "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json" },
            "order": [[0, "desc"]] // Sắp xếp theo ID mới nhất lên đầu
        });
    });
</script>
</body>
</html>