<%--
  File: select-ai-questions.jsp
  Purpose: Display a list of AI-generated questions for selection.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chọn câu hỏi từ AI</title>
    <%-- Sử dụng các file CSS từ template SB Admin 2 cho giao diện nhất quán --%>
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
                <h1 class="h3 mb-4 text-gray-800">Chọn câu hỏi từ AI để thêm vào ngân hàng câu hỏi</h1>

                <%-- Form để chọn và gửi các câu hỏi --%>
                <form action="${pageContext.request.contextPath}/admin/question/add_from_aiQuestion" method="post">
                    <div class="card shadow mb-4">
                        <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                            <h6 class="m-0 font-weight-bold text-primary">Danh sách câu hỏi do AI tạo</h6>
                            <button type="submit" class="btn btn-success btn-icon-split">
                                <span class="icon text-white-50"><i class="fas fa-plus"></i></span>
                                <span class="text">Thêm các câu hỏi đã chọn</span>
                            </button>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-bordered" id="aiQuestionsTable" width="100%" cellspacing="0">
                                    <thead>
                                    <tr>
                                        <th style="width: 50px;">Chọn</th>
                                        <th style="width: 70px;">ID</th>
                                        <th>Nội dung câu hỏi</th>
                                        <th>Độ khó</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="aiQuestion" items="${aiQuestions}">
                                        <tr>
                                            <td class="text-center">
                                                <input type="checkbox" name="selectedAiQuestionIds" value="${aiQuestion.id}">
                                            </td>
                                            <td>${aiQuestion.id}</td>
                                            <td>${aiQuestion.content}</td>
                                            <td>${aiQuestion.difficulty}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="footer.jsp" />
    </div>
</div>

<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>

<script src="${pageContext.request.contextPath}/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.js"></script>

<script>
    $(document).ready(function() {
        $('#aiQuestionsTable').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json"
            },
            "pageLength": 25,
            "order": [[ 1, "asc" ]],
            "columnDefs": [ {
                "targets": 0,
                "orderable": false
            } ]
        });
    });
</script>
</body>
</html>