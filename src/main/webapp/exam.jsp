<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- THÊM DÒNG NÀY --%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bài Kiểm Tra</title>
    <%-- Phần CSS của bạn giữ nguyên, không cần thay đổi --%>
    <style>
        /* ... TOÀN BỘ CSS CỦA BẠN ĐỂ Ở ĐÂY ... */
        * { margin: 0; padding: 0; box-sizing: border-box; } body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f5f5f5; color: #333; } .exam-container { display: flex; min-height: 100vh; } .question-sidebar { width: 250px; background-color: #fff; border-right: 1px solid #ddd; padding: 20px; box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1); } .sidebar-title { font-size: 18px; font-weight: bold; margin-bottom: 20px; color: #2c3e50; text-align: center; } .question-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; margin-bottom: 30px; } .question-number { width: 40px; height: 40px; border: 2px solid #3498db; background-color: #fff; display: flex; align-items: center; justify-content: center; cursor: pointer; font-weight: bold; border-radius: 8px; transition: all 0.3s ease; } .question-number:hover { background-color: #3498db; color: white; } .question-number.current { background-color: #e74c3c; color: white; border-color: #e74c3c; } .question-number.answered { background-color: #27ae60; color: white; border-color: #27ae60; } .submit-btn { width: 100%; padding: 15px; background-color: #e74c3c; color: white; border: none; border-radius: 8px; font-size: 16px; font-weight: bold; cursor: pointer; transition: background-color 0.3s ease; } .submit-btn:hover { background-color: #c0392b; } .question-area { flex: 1; padding: 30px; display: flex; flex-direction: column; } .exam-header { background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); margin-bottom: 30px; } .exam-title { font-size: 24px; color: #2c3e50; margin-bottom: 10px; } .exam-info { color: #7f8c8d; font-size: 14px; } .question-container { background-color: #fff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); flex: 1; margin-bottom: 20px; } .question-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; padding-bottom: 15px; border-bottom: 2px solid #ecf0f1; } .question-counter { font-size: 18px; color: #3498db; font-weight: bold; } .question-difficulty { padding: 5px 15px; border-radius: 20px; font-size: 12px; font-weight: bold; color: white; } .difficulty-1 { background-color: #27ae60; } .difficulty-2 { background-color: #f39c12; } .difficulty-3 { background-color: #e67e22; } .difficulty-4 { background-color: #e74c3c; } .difficulty-5 { background-color: #8e44ad; } .question-content { font-size: 18px; line-height: 1.6; margin-bottom: 25px; color: #2c3e50; } .options-container { margin-bottom: 30px; } .option { display: flex; align-items: center; padding: 15px; margin-bottom: 10px; border: 2px solid #ecf0f1; border-radius: 8px; cursor: pointer; transition: all 0.3s ease; background-color: #fff; } .option:hover { border-color: #3498db; background-color: #f8f9fa; } .option.selected { border-color: #3498db; background-color: #e3f2fd; } .option input[type="radio"] { margin-right: 15px; transform: scale(1.2); } .option-label { font-size: 16px; cursor: pointer; flex: 1; } .navigation-buttons { display: flex; justify-content: space-between; align-items: center; } .nav-btn { padding: 12px 25px; border: none; border-radius: 8px; font-size: 16px; cursor: pointer; transition: all 0.3s ease; font-weight: bold; } .prev-btn { background-color: #95a5a6; color: white; } .prev-btn:hover { background-color: #7f8c8d; } .next-btn { background-color: #3498db; color: white; } .next-btn:hover { background-color: #2980b9; } .nav-btn:disabled { background-color: #bdc3c7; cursor: not-allowed; } .question-navigation { display: flex; align-items: center; gap: 15px; } @media (max-width: 768px) { .exam-container { flex-direction: column; } .question-sidebar { width: 100%; border-right: none; border-bottom: 1px solid #ddd; } .question-grid { grid-template-columns: repeat(6, 1fr); } }
    </style>
</head>
<body>

<%-- Sử dụng c:set để tạo biến totalQuestions một cách an toàn --%>
<c:set var="questionList" value="${requestScope.questionList}"/>
<c:set var="totalQuestions" value="${not empty questionList ? questionList.size() : 0}"/>

<div class="exam-container">
    <div class="question-sidebar">
        <div class="sidebar-title">Danh sách câu hỏi</div>
        <div class="question-grid">
            <%-- Dùng c:forEach để lặp an toàn --%>
            <c:forEach begin="0" end="${totalQuestions - 1}" varStatus="loop">
                <div class="question-number ${loop.index == 0 ? 'current' : ''}"
                     onclick="goToQuestion(${loop.index})" id="q-${loop.index}">
                        ${loop.index + 1}
                </div>
            </c:forEach>
        </div>
        <button type="button" class="submit-btn" onclick="submitExam()">
            NỘP BÀI
        </button>
    </div>

    <div class="question-area">
        <div class="exam-header">
            <div class="exam-title">Bài Kiểm Tra Trắc Nghiệm</div>
            <div class="exam-info">
                Tổng số câu hỏi: ${totalQuestions} |
                Thời gian: <span id="timer">--:--</span>
            </div>
        </div>

        <%-- Dùng c:if để kiểm tra danh sách có rỗng hay không --%>
        <c:if test="${not empty questionList}">
            <form id="examForm" method="post" action="${pageContext.request.contextPath}/exam/submit">
                    <%-- Dùng c:forEach để lặp qua danh sách câu hỏi --%>
                <c:forEach items="${questionList}" var="question" varStatus="loop">
                    <c:set var="difficulty" value="${not empty question.difficulty ? question.difficulty : 3}"/>
                    <c:choose>
                        <c:when test="${difficulty == 1}">
                            <c:set var="difficultyText" value="Rất dễ"/>
                        </c:when>
                        <c:when test="${difficulty == 2}">
                            <c:set var="difficultyText" value="Dễ"/>
                        </c:when>
                        <c:when test="${difficulty == 4}">
                            <c:set var="difficultyText" value="Khó"/>
                        </c:when>
                        <c:when test="${difficulty == 5}">
                            <c:set var="difficultyText" value="Rất khó"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="difficultyText" value="Trung bình"/>
                        </c:otherwise>
                    </c:choose>

                    <div class="question-container" id="question-${loop.index}" style="${loop.index == 0 ? '' : 'display: none;'}">
                        <div class="question-header">
                            <div class="question-counter">
                                Câu ${loop.index + 1} / ${totalQuestions}
                            </div>
                            <div class="question-difficulty difficulty-${difficulty}">
                                    ${difficultyText}
                            </div>
                        </div>

                        <div class="question-content">
                                ${question.content}
                        </div>

                        <div class="options-container">
                                <%-- Dùng EL ${} thay vì scriptlet <%= %> --%>
                            <div class="option" onclick="selectOption(this, 'A', ${loop.index})">
                                <input type="radio" name="answer_${question.id}" value="A" id="q${loop.index}_a">
                                <label for="q${loop.index}_a" class="option-label">
                                    A. ${question.optionA}
                                </label>
                            </div>
                            <div class="option" onclick="selectOption(this, 'B', ${loop.index})">
                                <input type="radio" name="answer_${question.id}" value="B" id="q${loop.index}_b">
                                <label for="q${loop.index}_b" class="option-label">
                                    B. ${question.optionB}
                                </label>
                            </div>
                            <div class="option" onclick="selectOption(this, 'C', ${loop.index})">
                                <input type="radio" name="answer_${question.id}" value="C" id="q${loop.index}_c">
                                <label for="q${loop.index}_c" class="option-label">
                                    C. ${question.optionC}
                                </label>
                            </div>
                            <div class="option" onclick="selectOption(this, 'D', ${loop.index})">
                                <input type="radio" name="answer_${question.id}" value="D" id="q${loop.index}_d">
                                <label for="q${loop.index}_d" class="option-label">
                                    D. ${question.optionD}
                                </label>
                            </div>
                        </div>

                        <div class="navigation-buttons">
                            <button type="button" class="nav-btn prev-btn"
                                    onclick="previousQuestion()" ${loop.index == 0 ? 'disabled' : ''}>
                                ← Câu trước
                            </button>
                            <div class="question-navigation">
                                <span>Câu ${loop.index + 1} / ${totalQuestions}</span>
                            </div>
                            <button type="button" class="nav-btn next-btn"
                                    onclick="nextQuestion()" ${loop.last ? "style='display:none'" : ""}>
                                Câu tiếp →
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </form>
        </c:if>

        <c:if test="${empty questionList}">
            <div class="question-container">
                <div class="question-content">
                    Không có câu hỏi nào được tìm thấy hoặc đã có lỗi xảy ra.
                </div>
            </div>
        </c:if>
    </div>
</div>

<script>
    // Dùng biến JSTL để khởi tạo biến JS một cách an toàn
    const totalQuestions = ${totalQuestions};
    // ... Phần còn lại của JavaScript giữ nguyên ...
    let currentQuestion = 0;
    const userAnswers = {};

    function goToQuestion(questionIndex) {
        if(totalQuestions === 0) return;
        document.getElementById('question-' + currentQuestion).style.display = 'none';
        document.getElementById('question-' + questionIndex).style.display = 'block';
        document.getElementById('q-' + currentQuestion).classList.remove('current');
        document.getElementById('q-' + questionIndex).classList.add('current');
        currentQuestion = questionIndex;
        updateNavigationButtons();
    }

    function nextQuestion() {
        if (currentQuestion < totalQuestions - 1) {
            goToQuestion(currentQuestion + 1);
        }
    }

    function previousQuestion() {
        if (currentQuestion > 0) {
            goToQuestion(currentQuestion - 1);
        }
    }

    function selectOption(optionElement, optionValue, questionIndex) {
        const questionContainer = document.getElementById('question-' + questionIndex);
        const options = questionContainer.querySelectorAll('.option');
        options.forEach(opt => opt.classList.remove('selected'));
        optionElement.classList.add('selected');
        optionElement.querySelector('input[type="radio"]').checked = true;
        const questionIdInput = optionElement.querySelector('input[type="radio"]');
        const questionId = questionIdInput.name.split('_')[1];
        userAnswers[questionId] = optionValue;
        const questionNumber = document.getElementById('q-' + questionIndex);
        if (!questionNumber.classList.contains('current')) {
            questionNumber.classList.add('answered');
        }
    }

    function updateNavigationButtons() {
        if (totalQuestions === 0) return;
        const questionContainer = document.getElementById('question-' + currentQuestion);
        const prevBtn = questionContainer.querySelector('.prev-btn');
        const nextBtn = questionContainer.querySelector('.next-btn');

        prevBtn.disabled = (currentQuestion === 0);
        if (nextBtn) {
            nextBtn.style.display = (currentQuestion === totalQuestions - 1) ? 'none' : 'block';
        }
    }

    function submitExam() {
        if (totalQuestions === 0) {
            alert("Không có câu hỏi để nộp bài.");
            return;
        }
        const answeredCount = Object.keys(userAnswers).length;
        const unansweredCount = totalQuestions - answeredCount;

        let message = `Bạn đã trả lời ${answeredCount}/${totalQuestions} câu hỏi.`;
        if (unansweredCount > 0) {
            message += `\nCòn ${unansweredCount} câu chưa trả lời.`;
        }
        message += `\n\nBạn muốn làm thêm câu hỏi hay nộp bài ngay?`;

        document.getElementById('modalMessage').innerText = message;
        document.getElementById('submitModal').style.display = 'block';
    }

    function goToContinueExam() {
        closeModal();
        const dynamicForm = document.createElement("form");
        dynamicForm.method = "post";
        dynamicForm.action = "${pageContext.request.contextPath}/exam/addMore";

        for (const [questionId, answerValue] of Object.entries(userAnswers)) {
            const hidden = document.createElement("input");
            hidden.type = "hidden";
            hidden.name = "answer_" + questionId;
            hidden.value = answerValue;
            dynamicForm.appendChild(hidden);
        }

        const num = prompt("Bạn muốn thêm bao nhiêu câu hỏi?", "5");
        if (!num || isNaN(num) || parseInt(num) <= 0) {
            alert("Vui lòng nhập một số dương hợp lệ.");
            return;
        }
        const numInput = document.createElement("input");
        numInput.type = "hidden";
        numInput.name = "additionalQuestions";
        numInput.value = num;
        dynamicForm.appendChild(numInput);

        document.body.appendChild(dynamicForm);
        dynamicForm.submit();
    }

    function confirmFinalSubmit() {
        closeModal();
        const dynamicForm = document.createElement("form");
        dynamicForm.method = "post";
        dynamicForm.action = "${pageContext.request.contextPath}/exam/submit";

        for (const [questionId, answerValue] of Object.entries(userAnswers)) {
            const hidden = document.createElement("input");
            hidden.type = "hidden";
            hidden.name = "answer_" + questionId;
            hidden.value = answerValue;
            dynamicForm.appendChild(hidden);
        }
        document.body.appendChild(dynamicForm);
        dynamicForm.submit();
    }

    function closeModal() {
        document.getElementById('submitModal').style.display = 'none';
    }

    document.addEventListener('DOMContentLoaded', function () {
        if (totalQuestions > 0) {
            updateNavigationButtons();
        }
    });

    document.addEventListener('keydown', function (e) {
        if (e.key === 'ArrowLeft') {
            previousQuestion();
        } else if (e.key === 'ArrowRight') {
            nextQuestion();
        }
    });

</script>

<div id="submitModal" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background-color: rgba(0,0,0,0.4); z-index:1000;">
    <div style="background:#fff; margin:10% auto; padding:20px; border-radius:10px; max-width:400px; text-align:center;">
        <h3>Xác nhận nộp bài</h3>
        <p id="modalMessage"></p>
        <div style="margin-top:20px;">
            <button onclick="goToContinueExam()" style="margin: 5px; padding: 10px 20px;">Làm thêm</button>
            <button onclick="confirmFinalSubmit()" style="margin: 5px; padding: 10px 20px;">Nộp bài</button>
            <button onclick="closeModal()" style="margin: 5px; padding: 10px 20px;">Hủy</button>
        </div>
    </div>
</div>

</body>
</html>