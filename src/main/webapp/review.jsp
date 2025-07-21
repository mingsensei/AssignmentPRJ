<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Exam Result</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                display: flex;
                flex-direction: column;
                margin: 0;
                height: 100vh;
            }

            .sidebar {
                width: 200px;
                background-color: #f4f4f4;
                padding: 10px;
                overflow-y: auto;
                border-right: 1px solid #ddd;
                position: sticky;
                top: 0;
                height: 100vh;
            }

            .sidebar-item {
                display: block;
                padding: 10px;
                margin-bottom: 5px;
                text-decoration: none;
                color: #333;
                border-radius: 5px;
            }

            .sidebar-item.correct {
                background-color: #28a745;
                color: white;
            }

            .sidebar-item.incorrect {
                background-color: #dc3545;
                color: white;
            }

            .sidebar-item.unanswered {
                background-color: #6c757d;
                color: white;
            }

            .sidebar-item.active {
                outline: 2px solid #007bff;
            }

            .question-list {
                flex: 1;
                padding: 20px;
                overflow-y: auto;
                height: 100vh;
            }

            .question {
                margin-bottom: 40px;
                padding: 20px;
                border: 1px solid #ccc;
                border-radius: 10px;
                background-color: #fff;
            }

            .options li {
                padding: 5px;
                border-radius: 5px;
                margin: 5px 0;
                list-style: none;
            }

            .options li.correct {
                background-color: #d4edda;
            }

            .options li.incorrect {
                background-color: #f8d7da;
            }
        </style>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div style="display: flex;">
            <div class="sidebar">
                <c:forEach var="result" items="${results}" varStatus="loop">
                    <a class="sidebar-item ${result.status}" href="#q${loop.index}" id="link${loop.index}">
                        Q${loop.index + 1}
                    </a>
                </c:forEach>
            </div>
            <div class="question-list" id="questionList">
                <c:forEach var="result" items="${results}" varStatus="loop">
                    <div class="question" id="q${loop.index}">
                        <h2>Question ${loop.index + 1} ${result.AIQuestion ? "[AI]" : ""}</h2>
                        <p>${result.content}</p>

                        <ul class="options">
                            <li class="${result.correctOption == 'A' ? 'correct' : ''} ${result.studentAnswer == 'A' && result.correctOption != 'A' ? 'incorrect' : ''}">
                                A. ${result.optionA}
                            </li>
                            <li class="${result.correctOption == 'B' ? 'correct' : ''} ${result.studentAnswer == 'B' && result.correctOption != 'B' ? 'incorrect' : ''}">
                                B. ${result.optionB}
                            </li>
                            <li class="${result.correctOption == 'C' ? 'correct' : ''} ${result.studentAnswer == 'C' && result.correctOption != 'C' ? 'incorrect' : ''}">
                                C. ${result.optionC}
                            </li>
                            <li class="${result.correctOption == 'D' ? 'correct' : ''} ${result.studentAnswer == 'D' && result.correctOption != 'D' ? 'incorrect' : ''}">
                                D. ${result.optionD}
                            </li>
                        </ul>


                        <p><strong>Your Answer:</strong> ${result.studentAnswer}</p>
                        <p><strong>Correct Answer:</strong> ${result.correctOption}</p>
                    </div>
                </c:forEach>
            </div>
        </div>

        <%@ include file="footer.jsp" %>
        <script>
            const questions = document.querySelectorAll('.question');
            const sidebarLinks = document.querySelectorAll('.sidebar-item');

            const observer = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        sidebarLinks.forEach(link => link.classList.remove('active'));
                        const currentId = entry.target.id.replace('q', '');
                        const activeLink = document.getElementById('link' + currentId);
                        if (activeLink)
                            activeLink.classList.add('active');
                    }
                });
            }, {
                root: document.querySelector('#questionList'),
                threshold: 0.5
            });

            questions.forEach(question => observer.observe(question));
        </script>
    </body>
</html>
