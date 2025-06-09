<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.rf.dto.QuestionResponse" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bài Kiểm Tra</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f5f5;
            color: #333;
        }

        .exam-container {
            display: flex;
            min-height: 100vh;
        }

        /* Sidebar với danh sách câu hỏi */
        .question-sidebar {
            width: 250px;
            background-color: #fff;
            border-right: 1px solid #ddd;
            padding: 20px;
            box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
        }

        .sidebar-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 20px;
            color: #2c3e50;
            text-align: center;
        }

        .question-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 10px;
            margin-bottom: 30px;
        }

        .question-number {
            width: 40px;
            height: 40px;
            border: 2px solid #3498db;
            background-color: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            font-weight: bold;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .question-number:hover {
            background-color: #3498db;
            color: white;
        }

        .question-number.current {
            background-color: #e74c3c;
            color: white;
            border-color: #e74c3c;
        }

        .question-number.answered {
            background-color: #27ae60;
            color: white;
            border-color: #27ae60;
        }

        .submit-btn {
            width: 100%;
            padding: 15px;
            background-color: #e74c3c;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .submit-btn:hover {
            background-color: #c0392b;
        }

        /* Khu vực hiển thị câu hỏi */
        .question-area {
            flex: 1;
            padding: 30px;
            display: flex;
            flex-direction: column;
        }

        .exam-header {
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 30px;
        }

        .exam-title {
            font-size: 24px;
            color: #2c3e50;
            margin-bottom: 10px;
        }

        .exam-info {
            color: #7f8c8d;
            font-size: 14px;
        }

        .question-container {
            background-color: #fff;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            flex: 1;
            margin-bottom: 20px;
        }

        .question-header {
            display: flex;
            justify-content: between;
            align-items: center;
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 2px solid #ecf0f1;
        }

        .question-counter {
            font-size: 18px;
            color: #3498db;
            font-weight: bold;
        }

        .question-difficulty {
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
            color: white;
        }

        .difficulty-1 {
            background-color: #27ae60;
        }

        .difficulty-2 {
            background-color: #f39c12;
        }

        .difficulty-3 {
            background-color: #e67e22;
        }

        .difficulty-4 {
            background-color: #e74c3c;
        }

        .difficulty-5 {
            background-color: #8e44ad;
        }

        .question-content {
            font-size: 18px;
            line-height: 1.6;
            margin-bottom: 25px;
            color: #2c3e50;
        }

        .options-container {
            margin-bottom: 30px;
        }

        .option {
            display: flex;
            align-items: center;
            padding: 15px;
            margin-bottom: 10px;
            border: 2px solid #ecf0f1;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s ease;
            background-color: #fff;
        }

        .option:hover {
            border-color: #3498db;
            background-color: #f8f9fa;
        }

        .option.selected {
            border-color: #3498db;
            background-color: #e3f2fd;
        }

        .option input[type="radio"] {
            margin-right: 15px;
            transform: scale(1.2);
        }

        .option-label {
            font-size: 16px;
            cursor: pointer;
            flex: 1;
        }

        .navigation-buttons {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .nav-btn {
            padding: 12px 25px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-weight: bold;
        }

        .prev-btn {
            background-color: #95a5a6;
            color: white;
        }

        .prev-btn:hover {
            background-color: #7f8c8d;
        }

        .next-btn {
            background-color: #3498db;
            color: white;
        }

        .next-btn:hover {
            background-color: #2980b9;
        }

        .nav-btn:disabled {
            background-color: #bdc3c7;
            cursor: not-allowed;
        }

        .question-navigation {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .exam-container {
                flex-direction: column;
            }

            .question-sidebar {
                width: 100%;
                border-right: none;
                border-bottom: 1px solid #ddd;
            }

            .question-grid {
                grid-template-columns: repeat(6, 1fr);
            }
        }
    </style>
</head>
<body>
<%
    @SuppressWarnings("unchecked")
    List<QuestionResponse> questionList = (List<QuestionResponse>) request.getAttribute("questionList");
    int totalQuestions = questionList != null ? questionList.size() : 0;
%>

<div class="exam-container">
    <!-- Sidebar với danh sách câu hỏi -->
    <div class="question-sidebar">
        <div class="sidebar-title">Danh sách câu hỏi</div>
        <div class="question-grid">
            <% for (int i = 0; i < totalQuestions; i++) { %>
            <div class="question-number <%= i == 0 ? "current" : "" %>"
                 onclick="goToQuestion(<%= i %>)" id="q-<%= i %>">
                <%= i + 1 %>
            </div>
            <% } %>
        </div>
        <button type="button" class="submit-btn" onclick="submitExam()">
            NỘP BÀI
        </button>
    </div>

    <!-- Khu vực hiển thị câu hỏi -->
    <div class="question-area">
        <div class="exam-header">
            <div class="exam-title">Bài Kiểm Tra Trắc Nghiệm</div>
            <div class="exam-info">
                Tổng số câu hỏi: <%= totalQuestions %> |
                Thời gian: <span id="timer">--:--</span>
            </div>
        </div>

        <% if (questionList != null && !questionList.isEmpty()) { %>
        <form id="examForm" method="post" action="${pageContext.request.contextPath}/exam/submit">
            <% for (int i = 0; i < questionList.size(); i++) {
                QuestionResponse question = questionList.get(i);
                String difficultyClass = "difficulty-" + (question.getDifficulty() != null ? question.getDifficulty() : 1);
                String difficultyText = "";
                switch (question.getDifficulty() != null ? question.getDifficulty() : 1) {
                    case 1:
                        difficultyText = "Rất dễ";
                        break;
                    case 2:
                        difficultyText = "Dễ";
                        break;
                    case 3:
                        difficultyText = "Trung bình";
                        break;
                    case 4:
                        difficultyText = "Khó";
                        break;
                    case 5:
                        difficultyText = "Rất khó";
                        break;
                }
            %>
            <div class="question-container" id="question-<%= i %>" style="<%= i == 0 ? "" : "display: none;" %>">
                <div class="question-header">
                    <div class="question-counter">
                        Câu <%= i + 1 %> / <%= totalQuestions %>
                    </div>
                    <div class="question-difficulty <%= difficultyClass %>">
                        <%= difficultyText %>
                    </div>
                </div>

                <div class="question-content">
                    <%= question.getContent() %>
                </div>

                <div class="options-container">
                    <div class="option" onclick="selectOption(this, 'A', <%= i %>)">
                        <input type="radio" name="answer_<%= question.getId() %>" value="A" id="q<%= i %>_a">
                        <label for="q<%= i %>_a" class="option-label">
                            A. <%= question.getOptionA() %>
                        </label>
                    </div>

                    <div class="option" onclick="selectOption(this, 'B', <%= i %>)">
                        <input type="radio" name="answer_<%= question.getId() %>" value="B" id="q<%= i %>_b">
                        <label for="q<%= i %>_b" class="option-label">
                            B. <%= question.getOptionB() %>
                        </label>
                    </div>

                    <div class="option" onclick="selectOption(this, 'C', <%= i %>)">
                        <input type="radio" name="answer_<%= question.getId() %>" value="C" id="q<%= i %>_c">
                        <label for="q<%= i %>_c" class="option-label">
                            C. <%= question.getOptionC() %>
                        </label>
                    </div>

                    <div class="option" onclick="selectOption(this, 'D', <%= i %>)">
                        <input type="radio" name="answer_<%= question.getId() %>" value="D" id="q<%= i %>_d">
                        <label for="q<%= i %>_d" class="option-label">
                            D. <%= question.getOptionD() %>
                        </label>
                    </div>
                </div>

                <div class="navigation-buttons">
                    <button type="button" class="nav-btn prev-btn"
                            onclick="previousQuestion()"
                            <%= i == 0 ? "disabled" : "" %>>
                        ← Câu trước
                    </button>

                    <div class="question-navigation">
                        <span>Câu <%= i + 1 %> / <%= totalQuestions %></span>
                    </div>

                    <button type="button" class="nav-btn next-btn"
                            onclick="nextQuestion()"
                            <%= i == totalQuestions - 1 ? "style='display:none'" : "" %>>
                        Câu tiếp →
                    </button>
                </div>
            </div>
            <% } %>
        </form>
        <% } else { %>
        <div class="question-container">
            <div class="question-content">
                Không có câu hỏi nào được tìm thấy.
            </div>
        </div>
        <% } %>
    </div>
</div>

<script>
    let currentQuestion = 0;
    const totalQuestions = <%= totalQuestions %>;
    const userAnswers = {};

    function goToQuestion(questionIndex) {
        // Ẩn câu hỏi hiện tại
        document.getElementById('question-' + currentQuestion).style.display = 'none';

        // Hiện câu hỏi mới
        document.getElementById('question-' + questionIndex).style.display = 'block';

        // Cập nhật trạng thái sidebar
        document.getElementById('q-' + currentQuestion).classList.remove('current');
        document.getElementById('q-' + questionIndex).classList.add('current');

        // Cập nhật chỉ số câu hỏi hiện tại
        currentQuestion = questionIndex;

        // Cập nhật nút navigation
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
        // Xóa lựa chọn cũ
        const questionContainer = document.getElementById('question-' + questionIndex);
        const options = questionContainer.querySelectorAll('.option');
        options.forEach(opt => opt.classList.remove('selected'));

        // Thêm lựa chọn mới
        optionElement.classList.add('selected');
        optionElement.querySelector('input[type="radio"]').checked = true;

        // Lưu câu trả lời
        userAnswers[questionIndex] = optionValue;

        // Cập nhật trạng thái trong sidebar
        const questionNumber = document.getElementById('q-' + questionIndex);
        if (!questionNumber.classList.contains('current')) {
            questionNumber.classList.add('answered');
        }
    }

    function updateNavigationButtons() {
        const prevBtn = document.querySelector('.prev-btn');
        const nextBtn = document.querySelector('.next-btn');

        // Cập nhật nút Previous
        if (currentQuestion === 0) {
            prevBtn.disabled = true;
        } else {
            prevBtn.disabled = false;
        }

        // Cập nhật nút Next
        if (currentQuestion === totalQuestions - 1) {
            nextBtn.style.display = 'none';
        } else {
            nextBtn.style.display = 'block';
        }
    }

    function submitExam() {
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
        closeModal(); // Đóng modal

        // Tạo 1 form mới để gửi các đáp án và số câu muốn thêm
        const dynamicForm = document.createElement("form");
        dynamicForm.method = "post";
        dynamicForm.action = "<%= request.getContextPath() %>/exam/addMore";

        // Lấy tất cả đáp án đã chọn từ form gốc và bỏ vào form mới
        const selectedInputs = document.querySelectorAll("input[type='radio']:checked");
        selectedInputs.forEach(input => {
            const hidden = document.createElement("input");
            hidden.type = "hidden";
            hidden.name = input.name;
            hidden.value = input.value;
            dynamicForm.appendChild(hidden);
        });

        // Thêm số lượng câu hỏi muốn làm thêm
        const num = prompt("Bạn muốn thêm bao nhiêu câu hỏi?", "5");
        if (!num || isNaN(num)) {
            alert("Số không hợp lệ.");
            return;
        }
        const numInput = document.createElement("input");
        numInput.type = "hidden";
        numInput.name = "additionalQuestions";
        numInput.value = num;
        dynamicForm.appendChild(numInput);

        document.body.appendChild(dynamicForm);
        dynamicForm.submit(); // Gửi form đến /exam/addMore
    }

    function confirmFinalSubmit() {
        closeModal();
        const form = document.getElementById("examForm");
        form.submit(); // Gửi về /exam/submit như cũ
    }

    function closeModal() {
        document.getElementById('submitModal').style.display = 'none';
    }

    // Khởi tạo
    document.addEventListener('DOMContentLoaded', function () {
        updateNavigationButtons();
    });

    // Xử lý phím tắt
    document.addEventListener('keydown', function (e) {
        if (e.key === 'ArrowLeft') {
            previousQuestion();
        } else if (e.key === 'ArrowRight') {
            nextQuestion();
        }
    });
</script>
<!-- Modal xác nhận nộp bài -->
<div id="submitModal"
     style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background-color: rgba(0,0,0,0.4); z-index:1000;">
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