<%-- Created by IntelliJ IDEA. User: PC Date: 7/12/2025 Time: 5:34 PM To change this template use File | Settings | File Templates. --%>
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
                    <div class="tab-pane fade show active" id="ai-questions" role="tabpanel" aria-labelledby="ai-tab">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Danh sách câu hỏi do AI tạo</h6>
                            </div>
                            <div class="card-body">
                                <div class="mb-2">
                                    <label for="aiDifficultyFilter">Lọc theo độ khó:</label>
                                    <select id="aiDifficultyFilter" class="form-control" style="width: 200px; display: inline-block;">
                                        <option value="">Tất cả</option>
                                        <c:forEach var="diff" items="${aiQuestionDifficulties}">
                                            <option value="${diff}">${diff}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-bordered" id="aiQuestionsTable" width="100%" cellspacing="0">
                                        <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th style="width: min-content;">Nội dung câu hỏi</th>
                                            <th>Độ khó</th>
                                            <th>Hành động</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="aiQuestion" items="${aiQuestions}">
                                            <tr>
                                                <td>${aiQuestion.id}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${fn:length(aiQuestion.content) > 50}">
                                                            ${fn:substring(aiQuestion.content, 0, 50)}...
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${aiQuestion.content}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${aiQuestion.difficulty}</td>
                                                <td>
                                                    <!-- View Button -->
                                                    <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#viewAIQuestionModal${aiQuestion.id}">
                                                        <i class="fas fa-eye"></i> Xem
                                                    </button>
                                                    <!-- Edit Button -->
                                                    <button class="btn btn-sm btn-primary" data-toggle="modal" data-target="#editAIQuestionModal${aiQuestion.id}">
                                                        <i class="fas fa-edit"></i> Sửa
                                                    </button>
                                                    <!-- Delete Form -->
                                                    <form action="${pageContext.request.contextPath}/admin/question/delete" method="post" style="display:inline;" onsubmit="return confirm('Xác nhận xóa câu hỏi AI này?');">
                                                        <input type="hidden" name="action" value="delete" />
                                                        <input type="hidden" name="type" value="ai" />
                                                        <input type="hidden" name="id" value="${aiQuestion.id}" />
                                                        <button type="submit" class="btn btn-sm btn-danger">
                                                            <i class="fas fa-trash"></i> Xóa
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>

                                            <!-- View AI Question Modal -->
                                            <div class="modal fade" id="viewAIQuestionModal${aiQuestion.id}" tabindex="-1" role="dialog">
                                                <div class="modal-dialog modal-lg" role="document">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Chi tiết câu hỏi AI #${aiQuestion.id}</h5>
                                                            <button type="button" class="close" data-dismiss="modal">
                                                                <span>×</span>
                                                            </button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <div class="row">
                                                                <div class="col-md-6">
                                                                    <p><strong>ID:</strong> ${aiQuestion.id}</p>
                                                                    <p><strong>Độ khó:</strong> ${aiQuestion.difficulty}</p>
                                                                    <p><strong>Lựa chọn A: </strong> ${aiQuestion.optionA}</p>
                                                                    <p><strong>Lựa chọn B: </strong> ${aiQuestion.optionB}</p>
                                                                    <p><strong>Lựa chọn C: </strong> ${aiQuestion.optionC}</p>
                                                                    <p><strong>Lựa chọn D: </strong> ${aiQuestion.optionD}</p>
                                                                    <p><strong>Đáp án: </strong> ${aiQuestion.correctOption}</p>
                                                                </div>
                                                            </div>
                                                            <div class="row">
                                                                <div class="col-12">
                                                                    <p><strong>Nội dung câu hỏi:</strong></p>
                                                                    <p class="border p-3 bg-light">${aiQuestion.content}</p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <!-- Edit AI Question Modal -->
                                            <div class="modal fade" id="editAIQuestionModal${aiQuestion.id}" tabindex="-1" role="dialog">
                                                <div class="modal-dialog modal-lg" role="document">
                                                    <div class="modal-content">
                                                        <form method="post" action="${pageContext.request.contextPath}/admin/question/update_ai_question">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title">Sửa câu hỏi AI #${aiQuestion.id}</h5>
                                                                <button type="button" class="close" data-dismiss="modal">
                                                                    <span>×</span>
                                                                </button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <input type="hidden" name="action" value="update" />
                                                                <input type="hidden" name="type" value="ai" />
                                                                <input type="hidden" name="ai_question_id" value="${aiQuestion.id}" />

                                                                <div class="form-group">
                                                                    <label for="content${aiQuestion.id}">Nội dung câu hỏi <span class="text-danger">*</span></label>
                                                                    <textarea id="content${aiQuestion.id}" name="content" class="form-control" rows="4" required>${aiQuestion.content}</textarea>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label for="optionA${aiQuestion.id}">Đáp án A <span class="text-danger">*</span></label>
                                                                    <input type="text" id="optionA${aiQuestion.id}" name="optionA" class="form-control" value="${aiQuestion.optionA}" required />
                                                                </div>

                                                                <div class="form-group">
                                                                    <label for="optionB${aiQuestion.id}">Đáp án B <span class="text-danger">*</span></label>
                                                                    <input type="text" id="optionB${aiQuestion.id}" name="optionB" class="form-control" value="${aiQuestion.optionB}" required />
                                                                </div>

                                                                <div class="form-group">
                                                                    <label for="optionC${aiQuestion.id}">Đáp án C <span class="text-danger">*</span></label>
                                                                    <input type="text" id="optionC${aiQuestion.id}" name="optionC" class="form-control" value="${aiQuestion.optionC}" required />
                                                                </div>

                                                                <div class="form-group">
                                                                    <label for="optionD${aiQuestion.id}">Đáp án D <span class="text-danger">*</span></label>
                                                                    <input type="text" id="optionD${aiQuestion.id}" name="optionD" class="form-control" value="${aiQuestion.optionD}" required />
                                                                </div>

                                                                <div class="form-group">
                                                                    <label for="correctOption${aiQuestion.id}">Đáp án đúng <span class="text-danger">*</span></label>
                                                                    <select id="correctOption${aiQuestion.id}" name="correctOption" class="form-control" required>
                                                                        <option value="">Chọn đáp án đúng</option>
                                                                        <option value="A" ${aiQuestion.correctOption == 'A' ? 'selected' : ''}>A</option>
                                                                        <option value="B" ${aiQuestion.correctOption == 'B' ? 'selected' : ''}>B</option>
                                                                        <option value="C" ${aiQuestion.correctOption == 'C' ? 'selected' : ''}>C</option>
                                                                        <option value="D" ${aiQuestion.correctOption == 'D' ? 'selected' : ''}>D</option>
                                                                    </select>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label for="difficulty${aiQuestion.id}">Độ khó <span class="text-danger">*</span></label>
                                                                    <select id="difficulty${aiQuestion.id}" name="difficulty" class="form-control" required>
                                                                        <option value="">Chọn độ khó</option>
                                                                        <c:forEach var="diff" items="${aiQuestionDifficulties}">
                                                                            <option value="${diff}" ${aiQuestion.difficulty == diff ? 'selected' : ''}>${diff}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label for="explain${aiQuestion.id}">Giải thích</label>
                                                                    <textarea id="explain${aiQuestion.id}" name="explain" class="form-control" rows="3">${aiQuestion.explain}</textarea>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Lưu thay đổi</button>
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

                    <!-- Normal Questions Tab -->
                    <div class="tab-pane fade" id="normal-questions" role="tabpanel" aria-labelledby="normal-tab">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Danh sách câu hỏi</h6>
                                <button class="btn btn-primary" data-toggle="modal" data-target="#addQuestionModal">
                                    <i class="fas fa-plus"></i> Thêm câu hỏi
                                </button>
                            </div>
                            <div class="card-body">
                                <div class="mb-2">
                                    <label for="normalDifficultyFilter">Lọc theo độ khó:</label>
                                    <select id="normalDifficultyFilter" class="form-control" style="width: 200px; display: inline-block;">
                                        <option value="">Tất cả</option>
                                        <c:forEach var="diff" items="${questionDifficulties}">
                                            <option value="${diff}">${diff}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-bordered" id="normalQuestionsTable" width="100%" cellspacing="0">
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
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${fn:length(question.content) > 50}">
                                                            ${fn:substring(question.content, 0, 50)}...
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${question.content}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${question.difficulty}</td>
                                                <td>
                                                    <!-- View Button -->
                                                    <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#viewQuestionModal${question.id}">
                                                        <i class="fas fa-eye"></i> Xem
                                                    </button>
                                                    <!-- Edit Button -->
                                                    <button class="btn btn-sm btn-primary" data-toggle="modal" data-target="#editQuestionModal${question.id}">
                                                        <i class="fas fa-edit"></i> Sửa
                                                    </button>
                                                    <!-- Delete Form -->
                                                    <form action="${pageContext.request.contextPath}/admin/question/delete" method="post" style="display:inline;" onsubmit="return confirm('Xác nhận xóa câu hỏi này?');">
                                                        <input type="hidden" name="action" value="delete" />
                                                        <input type="hidden" name="type" value="normal" />
                                                        <input type="hidden" name="id" value="${question.id}" />
                                                        <button type="submit" class="btn btn-sm btn-danger">
                                                            <i class="fas fa-trash"></i> Xóa
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>

                                            <!-- View Normal Question Modal -->
                                            <div class="modal fade" id="viewQuestionModal${question.id}" tabindex="-1" role="dialog">
                                                <div class="modal-dialog modal-lg" role="document">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Chi tiết câu hỏi #${question.id}</h5>
                                                            <button type="button" class="close" data-dismiss="modal">
                                                                <span>×</span>
                                                            </button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <div class="row">
                                                                <div class="col-md-6">
                                                                    <p><strong>ID:</strong> ${question.id}</p>
                                                                    <p><strong>Độ khó:</strong> ${question.difficulty}</p>
                                                                    <p><strong>Chương:</strong> ${question.chapterId}</p>
                                                                    <p><strong>Đáp án đúng:</strong> ${question.correctOption}</p>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <p><strong>Đáp án A:</strong> ${question.optionA}</p>
                                                                    <p><strong>Đáp án B:</strong> ${question.optionB}</p>
                                                                    <p><strong>Đáp án C:</strong> ${question.optionC}</p>
                                                                    <p><strong>Đáp án D:</strong> ${question.optionD}</p>
                                                                </div>
                                                            </div>
                                                            <div class="row">
                                                                <div class="col-12">
                                                                    <p><strong>Nội dung câu hỏi:</strong></p>
                                                                    <p class="border p-3 bg-light">${question.content}</p>
                                                                </div>
                                                            </div>
                                                            <div class="row">
                                                                <div class="col-12">
                                                                    <p><strong>Giải thích:</strong></p>
                                                                    <p class="border p-3 bg-light">${question.explain}</p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <!-- Edit Normal Question Modal -->
                                            <div class="modal fade" id="editQuestionModal${question.id}" tabindex="-1" role="dialog">
                                                <div class="modal-dialog modal-lg" role="document">
                                                    <div class="modal-content">
                                                        <form method="post" action="${pageContext.request.contextPath}/admin/question/update">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title">Sửa câu hỏi #${question.id}</h5>
                                                                <button type="button" class="close" data-dismiss="modal">
                                                                    <span>×</span>
                                                                </button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <input type="hidden" name="action" value="update" />
                                                                <input type="hidden" name="type" value="normal" />
                                                                <input type="hidden" name="id" value="${question.id}" />
                                                                <div class="form-group">
                                                                    <label for="content${question.id}">Nội dung câu hỏi <span class="text-danger">*</span></label>
                                                                    <textarea id="content${question.id}" name="content" class="form-control" rows="3" required>${question.content}</textarea>
                                                                </div>
                                                                <div class="row">
                                                                    <div class="col-md-6">
                                                                        <div class="form-group">
                                                                            <label for="optionA${question.id}">Đáp án A <span class="text-danger">*</span></label>
                                                                            <input type="text" id="optionA${question.id}" name="optionA" class="form-control" value="${question.optionA}" required>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label for="optionB${question.id}">Đáp án B <span class="text-danger">*</span></label>
                                                                            <input type="text" id="optionB${question.id}" name="optionB" class="form-control" value="${question.optionB}" required>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <div class="form-group">
                                                                            <label for="optionC${question.id}">Đáp án C <span class="text-danger">*</span></label>
                                                                            <input type="text" id="optionC${question.id}" name="optionC" class="form-control" value="${question.optionC}" required>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label for="optionD${question.id}">Đáp án D <span class="text-danger">*</span></label>
                                                                            <input type="text" id="optionD${question.id}" name="optionD" class="form-control" value="${question.optionD}" required>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="row">
                                                                    <div class="col-md-4">
                                                                        <div class="form-group">
                                                                            <label for="correctOption${question.id}">Đáp án đúng <span class="text-danger">*</span></label>
                                                                            <select id="correctOption${question.id}" name="correctOption" class="form-control" required>
                                                                                <option value="">Chọn đáp án</option>
                                                                                <option value="A" ${question.correctOption == 'A' ? 'selected' : ''}>A</option>
                                                                                <option value="B" ${question.correctOption == 'B' ? 'selected' : ''}>B</option>
                                                                                <option value="C" ${question.correctOption == 'C' ? 'selected' : ''}>C</option>
                                                                                <option value="D" ${question.correctOption == 'D' ? 'selected' : ''}>D</option>
                                                                            </select>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-md-4">
                                                                        <div class="form-group">
                                                                            <label for="difficulty${question.id}">Độ khó <span class="text-danger">*</span></label>
                                                                            <select id="difficulty${question.id}" name="difficulty" class="form-control" required>
                                                                                <option value="">Chọn độ khó</option>
                                                                                <option value="1" ${question.difficulty == '1' ? 'selected' : ''}>Dễ</option>
                                                                                <option value="2" ${question.difficulty == '2' ? 'selected' : ''}>Trung bình</option>
                                                                                <option value="3" ${question.difficulty == '3' ? 'selected' : ''}>Khó</option>
                                                                            </select>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-md-4">
                                                                        <div class="form-group">
                                                                            <label for="chapterId${question.id}">Chương <span class="text-danger">*</span></label>
                                                                            <select id="chapterId${question.id}" name="chapterId" class="form-control" required>
                                                                                <option value="">Chọn chương</option>
                                                                                <c:forEach var="chapter" items="${chapters}">
                                                                                    <option value="${chapter.id}" ${question.chapterId == chapter.id ? 'selected' : ''}>${chapter.name}</option>
                                                                                </c:forEach>
                                                                            </select>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="explain${question.id}">Giải thích</label>
                                                                    <textarea id="explain${question.id}" name="explain" class="form-control" rows="2">${question.explain}</textarea>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Lưu thay đổi</button>
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
            </div>
            <jsp:include page="footer.jsp" />
        </div>
    </div>
</div>

<!-- Add Question Modal -->
<div class="modal fade" id="addQuestionModal" tabindex="-1" role="dialog" aria-labelledby="addQuestionModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addQuestionModalLabel">Thêm câu hỏi</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="text-center mb-4">
                    <div class="btn-group" role="group">
                        <button type="button" class="btn btn-outline-primary" id="btnNewQuestion">
                            <i class="fas fa-plus"></i> Tạo câu hỏi mới
                        </button>
                        <button type="button" class="btn btn-outline-success" id="btnConvertAI">
                            <i class="fas fa-robot"></i> Chuyển đổi AI Question
                        </button>
                    </div>
                </div>

                <!-- New Question Form -->
                <div id="newQuestionForm" style="display:none;">
                    <form id="questionForm" action="${pageContext.request.contextPath}/admin/question/add" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="type" value="normal">
                        <div id="questionsContainer">
                            <div class="question-item border p-3 mb-3">
                                <h6 class="text-primary">Câu hỏi #1</h6>
                                <div class="form-group">
                                    <label>Nội dung câu hỏi <span class="text-danger">*</span></label>
                                    <textarea class="form-control" name="content[]" rows="3" required></textarea>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Đáp án A <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control" name="optionA[]" required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Đáp án B <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control" name="optionB[]" required>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Đáp án C <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control" name="optionC[]" required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Đáp án D <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control" name="optionD[]" required>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-3">
                                        <div class="form-group">
                                            <label>Đáp án đúng <span class="text-danger">*</span></label>
                                            <select class="form-control" name="correctOption[]" required>
                                                <option value="">Chọn đáp án</option>
                                                <option value="A">A</option>
                                                <option value="B">B</option>
                                                <option value="C">C</option>
                                                <option value="D">D</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="form-group">
                                            <label>Độ khó <span class="text-danger">*</span></label>
                                            <select class="form-control" name="difficulty[]" required>
                                                <option value="">Chọn độ khó</option>
                                                <option value="1">Rất dễ</option>
                                                <option value="2">Dễ</option>
                                                <option value="3">Trung bình</option>
                                                <option value="4">Khó</option>
                                                <option value="5">Rất khó</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="form-group">
                                            <label>Môn học <span class="text-danger">*</span></label>
                                            <select class="form-control course-select" name="courseId[]" required>
                                                <option value="">Chọn môn học</option>
                                                <c:forEach var="course" items="${courses}">
                                                    <option value="${course.id}">${course.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="form-group">
                                            <label>Chương <span class="text-danger">*</span></label>
                                            <select class="form-control chapter-select" name="chapterId[]" required disabled>
                                                <option value="">Chọn chương</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label>Giải thích</label>
                                    <textarea class="form-control" name="explain[]" rows="2"></textarea>
                                </div>
                                <div class="text-right">
                                    <button type="button" class="btn btn-sm btn-danger remove-question" style="display:none;">
                                        <i class="fas fa-trash"></i> Xóa câu hỏi
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="text-center mb-3">
                            <button type="button" class="btn btn-success" id="addMoreQuestion">
                                <i class="fas fa-plus"></i> Thêm câu hỏi
                            </button>
                        </div>
                    </form>
                </div>

                <!-- Convert AI Question Form -->
                <div id="convertAIForm" style="display:none;">
                    <form id="convertForm" action="${pageContext.request.contextPath}/admin/question/addFromAiQuestion" method="post">
                        <input type="hidden" name="action" value="convert">
                        <input type="hidden" name="type" value="ai-to-normal">
                        <div class="form-group">
                            <label>Chọn câu hỏi AI để chuyển đổi:</label>
                            <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                                <table class="table table-sm table-bordered">
                                    <thead>
                                    <tr>
                                        <th width="50">
                                            <input type="checkbox" id="selectAllAI">
                                        </th>
                                        <th>ID</th>
                                        <th>Nội dung</th>
                                        <th>Độ khó</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="aiQuestion" items="${aiQuestions}">
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="selectedAIQuestions" value="${aiQuestion.id}">
                                            </td>
                                            <td>${aiQuestion.id}</td>
                                            <td>${fn:substring(aiQuestion.content, 0, 100)}...</td>
                                            <td>${aiQuestion.difficulty}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Chọn chương cho các câu hỏi được chuyển đổi <span class="text-danger">*</span></label>
                            <select class="form-control" name="targetChapterId" required>
                                <option value="">Chọn chương</option>
                                <c:forEach var="chapter" items="${chapters}">
                                    <option value="${chapter.id}">${chapter.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                <button type="button" class="btn btn-primary" id="submitForm">Lưu</button>
            </div>
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
            "order": [[0, "asc"]]
        });

        // DataTable for Normal Questions
        var normalTable = $('#normalQuestionsTable').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json"
            },
            "pageLength": 25,
            "order": [[0, "asc"]]
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
            $.fn.dataTable.tables({ visible: true, api: true }).columns.adjust();
        });

        // Add Question Modal Functions
        let questionCounter = 1;
        let currentMode = '';

        // Toggle between new question and convert AI forms
        $('#btnNewQuestion').click(function() {
            currentMode = 'new';
            $(this).removeClass('btn-outline-primary').addClass('btn-primary');
            $('#btnConvertAI').removeClass('btn-success').addClass('btn-outline-success');
            $('#newQuestionForm').show();
            $('#convertAIForm').hide();
        });

        $('#btnConvertAI').click(function() {
            currentMode = 'convert';
            $(this).removeClass('btn-outline-success').addClass('btn-success');
            $('#btnNewQuestion').removeClass('btn-primary').addClass('btn-outline-primary');
            $('#convertAIForm').show();
            $('#newQuestionForm').hide();
        });

        // Handle course selection change
        $(document).on('change', '.course-select', function() {
            const courseId = $(this).val();
            const chapterSelect = $(this).closest('.question-item').find('.chapter-select');
            if (courseId) {
                // Enable chapter dropdown
                chapterSelect.prop('disabled', false);
                // Clear previous options
                chapterSelect.html('<option value="">Đang tải...</option>');
                // Fetch chapters via AJAX
                $.ajax({
                    url: '${pageContext.request.contextPath}/chapters-by-course',
                    method: 'GET',
                    data: { courseId: courseId },
                    dataType: 'json',
                    success: function(chapters) {
                        chapterSelect.html('<option value="">Chọn chương</option>');
                        if (chapters && chapters.length > 0) {
                            chapters.forEach(function(chapter) {
                                chapterSelect.append('<option value="' + chapter.id + '">' + chapter.title + '</option>');
                            });
                        } else {
                            chapterSelect.html('<option value="">Không có chương nào</option>');
                        }
                    },
                    error: function() {
                        chapterSelect.html('<option value="">Lỗi khi tải chương</option>');
                    }
                });
            } else {
                // Disable and reset chapter dropdown
                chapterSelect.prop('disabled', true);
                chapterSelect.html('<option value="">Chọn chương</option>');
            }
        });

        // Add more questions
        $('#addMoreQuestion').click(function() {
            questionCounter++;
            const questionTemplate = `
            <div class="question-item border p-3 mb-3">
                <h6 class="text-primary">Câu hỏi #${questionCounter}</h6>
                <div class="form-group">
                    <label>Nội dung câu hỏi <span class="text-danger">*</span></label>
                    <textarea class="form-control" name="content[]" rows="3" required></textarea>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Đáp án A <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="optionA[]" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Đáp án B <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="optionB[]" required>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Đáp án C <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="optionC[]" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Đáp án D <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="optionD[]" required>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3">
                        <div class="form-group">
                            <label>Đáp án đúng <span class="text-danger">*</span></label>
                            <select class="form-control" name="correctOption[]" required>
                                <option value="">Chọn đáp án</option>
                                <option value="A">A</option>
                                <option value="B">B</option>
                                <option value="C">C</option>
                                <option value="D">D</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label>Độ khó <span class="text-danger">*</span></label>
                            <select class="form-control" name="difficulty[]" required>
                                <option value="">Chọn độ khó</option>
                                <option value="1">Rất dễ</option>
                                <option value="2">Dễ</option>
                                <option value="3">Trung bình</option>
                                <option value="4">Khó</option>
                                <option value="5">Rất khó</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label>Môn học <span class="text-danger">*</span></label>
                            <select class="form-control course-select" name="courseId[]" required>
                                <option value="">Chọn môn học</option>
                                <c:forEach var="course" items="${courses}">
                                    <option value="${course.id}">${course.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label>Chương <span class="text-danger">*</span></label>
                            <select class="form-control chapter-select" name="chapterId[]" required disabled>
                                <option value="">Chọn chương</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label>Giải thích</label>
                    <textarea class="form-control" name="explain[]" rows="2"></textarea>
                </div>
                <div class="text-right">
                    <button type="button" class="btn btn-sm btn-danger remove-question">
                        <i class="fas fa-trash"></i> Xóa câu hỏi
                    </button>
                </div>
            </div>
        `;
            $('#questionsContainer').append(questionTemplate);
            updateRemoveButtons();
        });

        // Remove question
        $(document).on('click', '.remove-question', function() {
            $(this).closest('.question-item').remove();
            updateQuestionNumbers();
            updateRemoveButtons();
        });

        // Update question numbers
        function updateQuestionNumbers() {
            $('.question-item').each(function(index) {
                $(this).find('h6').text('Câu hỏi #' + (index + 1));
            });
            questionCounter = $('.question-item').length;
        }

        // Update remove buttons visibility
        function updateRemoveButtons() {
            const questionCount = $('.question-item').length;
            if (questionCount > 1) {
                $('.remove-question').show();
            } else {
                $('.remove-question').hide();
            }
        }

        // Select all AI questions
        $('#selectAllAI').change(function() {
            $('input[name="selectedAIQuestions"]').prop('checked', this.checked);
        });

        // Submit form
        $('#submitForm').click(function() {
            if (currentMode === 'new') {
                if (validateNewQuestionForm()) {
                    $('#questionForm').submit();
                }
            } else if (currentMode === 'convert') {
                if (validateConvertForm()) {
                    $('#convertForm').submit();
                }
            } else {
                alert('Vui lòng chọn loại thêm câu hỏi!');
            }
        });

        // Validation functions
        function validateNewQuestionForm() {
            let isValid = true;
            let errorMessages = [];
            $('.question-item').each(function(index) {
                const questionNum = index + 1;
                const content = $(this).find('textarea[name="content[]"]').val().trim();
                const optionA = $(this).find('input[name="optionA[]"]').val().trim();
                const optionB = $(this).find('input[name="optionB[]"]').val().trim();
                const optionC = $(this).find('input[name="optionC[]"]').val().trim();
                const optionD = $(this).find('input[name="optionD[]"]').val().trim();
                const correctOption = $(this).find('select[name="correctOption[]"]').val();
                const difficulty = $(this).find('select[name="difficulty[]"]').val();
                const courseId = $(this).find('select[name="courseId[]"]').val();
                const chapterId = $(this).find('select[name="chapterId[]"]').val();

                if (!content) {
                    errorMessages.push(`Câu hỏi ${questionNum}: Nội dung câu hỏi không được trống`);
                    isValid = false;
                }
                if (!optionA) {
                    errorMessages.push(`Câu hỏi ${questionNum}: Đáp án A không được trống`);
                    isValid = false;
                }
                if (!optionB) {
                    errorMessages.push(`Câu hỏi ${questionNum}: Đáp án B không được trống`);
                    isValid = false;
                }
                if (!optionC) {
                    errorMessages.push(`Câu hỏi ${questionNum}: Đáp án C không được trống`);
                    isValid = false;
                }
                if (!optionD) {
                    errorMessages.push(`Câu hỏi ${questionNum}: Đáp án D không được trống`);
                    isValid = false;
                }
                if (!correctOption) {
                    errorMessages.push(`Câu hỏi ${questionNum}: Vui lòng chọn đáp án đúng`);
                    isValid = false;
                }
                if (!difficulty) {
                    errorMessages.push(`Câu hỏi ${questionNum}: Vui lòng chọn độ khó`);
                    isValid = false;
                }
                if (!courseId) {
                    errorMessages.push(`Câu hỏi ${questionNum}: Vui lòng chọn môn học`);
                    isValid = false;
                }
                if (!chapterId) {
                    errorMessages.push(`Câu hỏi ${questionNum}: Vui lòng chọn chương`);
                    isValid = false;
                }
            });

            if (!isValid) {
                alert('Vui lòng kiểm tra lại:\n' + errorMessages.join('\n'));
            }
            return isValid;
        }

        function validateConvertForm() {
            let isValid = true;
            let errorMessages = [];
            const selectedQuestions = $('input[name="selectedAIQuestions"]:checked').length;
            if (selectedQuestions === 0) {
                errorMessages.push('Vui lòng chọn ít nhất một câu hỏi AI để chuyển đổi');
                isValid = false;
            }
            const targetChapterId = $('select[name="targetChapterId"]').val();
            if (!targetChapterId) {
                errorMessages.push('Vui lòng chọn chương đích cho các câu hỏi được chuyển đổi');
                isValid = false;
            }
            if (!isValid) {
                alert('Vui lòng kiểm tra lại:\n' + errorMessages.join('\n'));
            }
            return isValid;
        }

        // Reset modal when closed
        $('#addQuestionModal').on('hidden.bs.modal', function () {
            currentMode = '';
            $('#btnNewQuestion').removeClass('btn-primary').addClass('btn-outline-primary');
            $('#btnConvertAI').removeClass('btn-success').addClass('btn-outline-success');
            $('#newQuestionForm').hide();
            $('#convertAIForm').hide();
            $('#questionForm')[0].reset();
            $('.question-item').not(':first').remove();
            questionCounter = 1;
            updateQuestionNumbers();
            updateRemoveButtons();
            $('#convertForm')[0].reset();
            $('#selectAllAI').prop('checked', false);
            $('input[name="selectedAIQuestions"]').prop('checked', false);
        });

        // Handle individual checkbox changes for AI questions
        $(document).on('change', 'input[name="selectedAIQuestions"]', function() {
            const totalCheckboxes = $('input[name="selectedAIQuestions"]').length;
            const checkedCheckboxes = $('input[name="selectedAIQuestions"]:checked').length;
            if (checkedCheckboxes === totalCheckboxes) {
                $('#selectAllAI').prop('checked', true);
            } else {
                $('#selectAllAI').prop('checked', false);
            }
        });

        // Show success message if exists
        <c:if test="${not empty successMessage}">
        alert('${successMessage}');
        </c:if>

        // Show error message if exists
        <c:if test="${not empty errorMessage}">
        alert('${errorMessage}');
        </c:if>
    });
</script>
</body>
</html>