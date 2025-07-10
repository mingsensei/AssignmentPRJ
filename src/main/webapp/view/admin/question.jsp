<%--
  File: question.jsp
  Purpose: Display a list of AI-generated questions and normal questions with filtering by difficulty.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Quản lý câu hỏi</title>
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
                <h1 class="h3 mb-4 text-gray-800">Quản lý câu hỏi</h1>
                <!-- Tabs for AI Questions and Questions -->
                <ul class="nav nav-tabs mb-3" id="questionTabs" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" id="ai-tab" data-toggle="tab" href="#ai-questions" role="tab"
                           aria-controls="ai-questions" aria-selected="true">AI Question</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" id="normal-tab" data-toggle="tab" href="#normal-questions" role="tab"
                           aria-controls="normal-questions" aria-selected="false">Question</a>
                    </li>
                </ul>
                <div class="tab-content" id="questionTabsContent">
                    <!-- AI Questions Tab -->
                    <div class="tab-pane fade show active" id="ai-questions" role="tabpanel"
                         aria-labelledby="ai-tab">
                        <div class="card shadow mb-4">
                            <div
                                    class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Danh sách câu hỏi do AI tạo</h6>
                            </div>
                            <div class="card-body">
                                <div class="mb-2">
                                    <label for="aiDifficultyFilter">Lọc theo độ khó:</label>
                                    <select id="aiDifficultyFilter" class="form-control"
                                            style="width: 200px; display: inline-block;">
                                        <option value="">Tất cả</option>
                                        <c:forEach var="diff" items="${aiQuestionDifficulties}">
                                            <option value="${diff}">${diff}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-bordered" id="aiQuestionsTable" width="100%"
                                           cellspacing="0">
                                        <thead>
                                        <tr>
                                            <th style="width: 70px;">ID</th>
                                            <th>Nội dung câu hỏi</th>
                                            <th>Độ khó</th>
                                            <th>Hành động</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="aiQuestion" items="${aiQuestions}">
                                            <tr>
                                                <td>${aiQuestion.id}</td>
                                                <td>${aiQuestion.content}</td>
                                                <td>${aiQuestion.difficulty}</td>
                                                <td>
                                                    <button class="btn btn-sm btn-info"
                                                            onclick="location.href='${pageContext.request.contextPath}/admin/question?action=view&type=ai&id=${aiQuestion.id}'"
                                                            type="button"><i class="fas fa-eye"></i>
                                                        Xem</button>
                                                    <button class="btn btn-sm btn-primary"
                                                            onclick="location.href='${pageContext.request.contextPath}/admin/question?action=edit&type=ai&id=${aiQuestion.id}'"
                                                            type="button"><i class="fas fa-edit"></i>
                                                        Sửa</button>
                                                    <form
                                                            action="${pageContext.request.contextPath}/admin/question"
                                                            method="post" style="display:inline;"
                                                            onsubmit="return confirm('Xác nhận xóa câu hỏi AI này?');">
                                                        <input type="hidden" name="action" value="delete" />
                                                        <input type="hidden" name="type" value="ai" />
                                                        <input type="hidden" name="id"
                                                               value="${aiQuestion.id}" />
                                                        <button type="submit"
                                                                class="btn btn-sm btn-danger"><i
                                                                class="fas fa-trash"></i> Xóa</button>
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
                    <!-- Normal Questions Tab -->
                    <div class="tab-pane fade" id="normal-questions" role="tabpanel" aria-labelledby="normal-tab">
                        <div class="card shadow mb-4">
                            <div
                                    class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Danh sách câu hỏi</h6>
                            </div>
                            <div class="card-body">
                                <div class="mb-2">
                                    <label for="normalDifficultyFilter">Lọc theo độ khó:</label>
                                    <select id="normalDifficultyFilter" class="form-control"
                                            style="width: 200px; display: inline-block;">
                                        <option value="">Tất cả</option>
                                        <c:forEach var="diff" items="${questionDifficulties}">
                                            <option value="${diff}">${diff}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-bordered" id="normalQuestionsTable" width="100%"
                                           cellspacing="0">
                                        <thead>
                                        <tr>
                                            <th style="width: 70px;">ID</th>
                                            <th>Nội dung câu hỏi</th>
                                            <th>Độ khó</th>
                                            <th>Hành động</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="question" items="${questions}">
                                            <tr>
                                                <td>${question.id}</td>
                                                <td>${question.content}</td>
                                                <td>${question.difficulty}</td>
                                                <td>
                                                    <button class="btn btn-sm btn-info"
                                                            onclick="location.href='${pageContext.request.contextPath}/admin/question?action=view&type=normal&id=${question.id}'"
                                                            type="button"><i class="fas fa-eye"></i> Xem</button>
                                                    <button class="btn btn-sm btn-primary"
                                                            onclick="location.href='${pageContext.request.contextPath}/admin/question?action=edit&type=normal&id=${question.id}'"
                                                            type="button"><i class="fas fa-edit"></i> Sửa</button>
                                                    <form
                                                            action="${pageContext.request.contextPath}/admin/question"
                                                            method="post" style="display:inline;"
                                                            onsubmit="return confirm('Xác nhận xóa câu hỏi này?');">
                                                        <input type="hidden" name="action" value="delete" />
                                                        <input type="hidden" name="type" value="normal" />
                                                        <input type="hidden" name="id" value="${question.id}" />
                                                        <button type="submit" class="btn btn-sm btn-danger"><i
                                                                class="fas fa-trash"></i> Xóa</button>
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
            </div>
            <jsp:include page="footer.jsp" />
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.js"></script>
<script>
    $(document).ready(function () {
        // DataTable for AI Questions
        var aiTable = $('#aiQuestionsTable').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json"
            },
            "pageLength": 25,
            "order": [
                [1, "asc"]
            ],
            "columnDefs": [{
                "targets": 0,
                "orderable": false
            }]
        });
        // DataTable for Normal Questions
        var normalTable = $('#normalQuestionsTable').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json"
            },
            "pageLength": 25,
            "order": [
                [0, "asc"]
            ]
        });
        // Filter for AI Questions
        $('#aiDifficultyFilter').on('change', function () {
            var val = $(this).val();
            if (val) {
                aiTable.column(2).search('^' + val + '$', true, false).draw();
            } else {
                aiTable.column(2).search('').draw();
            }
        });
        // Filter for Normal Questions
        $('#normalDifficultyFilter').on('change', function () {
            var val = $(this).val();
            if (val) {
                normalTable.column(2).search('^' + val + '$', true, false).draw();
            } else {
                normalTable.column(2).search('').draw();
            }
        });
        // Activate correct tab on reload
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $.fn.dataTable.tables({
                visible: true,
                api: true
            }).columns.adjust();
        });
    });
</script>
</body>

</html>