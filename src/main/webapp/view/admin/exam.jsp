<%--
  File: exam-results.jsp
  Purpose: Display student exam results and history.
  Date: 7/1/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lịch sử làm bài</title>
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,300,400,700" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">
</head>
<body id="page-top">
<div id="wrapper">
    <%-- Include Sidebar --%>
    <jsp:include page="sidebar.jsp" />

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <%-- Include Topbar --%>
            <jsp:include page="topbar.jsp" />

            <div class="container-fluid">
                <%-- Page Header --%>
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Lịch sử làm bài của sinh viên</h1>
                    <a href="${pageContext.request.contextPath}/admin/course" class="btn btn-sm btn-secondary">
                        <i class="fas fa-arrow-left"></i> Quay lại
                    </a>
                </div>

                <%-- Exam Results Table --%>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Danh sách kết quả</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="examResultsTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Mã Sinh viên</th>
                                    <th>Mã Chương</th>
                                    <th>Điểm số</th>
                                    <th>Thời gian nộp bài</th>
                                    <th>Hành động</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="result" items="${examResults}">
                                    <tr>
                                        <td>${result.id}</td>
                                        <td>${result.studentId}</td>
                                        <td>${result.chapterId}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty result.score}">
                                                    <span class="badge badge-success">${result.score}/100</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-warning">Chưa chấm</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${result.submittedAtAsDate}" pattern="HH:mm:ss dd/MM/yyyy" />
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/exam/details?id=${result.id}" class="btn btn-sm btn-info">
                                                <i class="fas fa-eye"></i> Xem chi tiết
                                            </a>
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

        <%-- Include Footer --%>
        <jsp:include page="footer.jsp" />
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
        $('#examResultsTable').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json"
            },
            "pageLength": 10,
            // Sort by submission time descending (newest first)
            "order": [[ 4, "desc" ]]
        });
    });
</script>
</body>
</html>