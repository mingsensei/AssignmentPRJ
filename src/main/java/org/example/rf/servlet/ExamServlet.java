package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.dto.QuestionResponse;
import org.example.rf.service.ExamService;
import org.example.rf.service.LevelService;
import org.example.rf.service.QuestionService;

import java.io.IOException;
import java.util.List;

@WebServlet("/exam/*")
public class ExamServlet extends HttpServlet {

    private final ExamService examService = new ExamService();
    private final LevelService levelService = new LevelService();
    private final QuestionService questionService = new QuestionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            handleExamSetupGet(request, response);
        } else if (pathInfo.equals("/setup")) {
            handleExamSetupGet(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            handleExamPost(request, response);
        }
    }

    private void handleExamSetupGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String chapterId = request.getParameter("chapterId");
        request.setAttribute("chapterId", chapterId);
        request.getRequestDispatcher("/setupExam.jsp").forward(request, response);
    }

    private void handleExamPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String studentIdParam = (String) request.getSession().getAttribute("studentId");
        String numQuestionsParam = request.getParameter("numQuestionsParam");
        String chapterIdParam = request.getParameter("chapterId");
        String difficultyParam = request.getParameter("difficulty");
        String examId = (String) request.getSession().getAttribute("examId");

        if (chapterIdParam == null || chapterIdParam.isEmpty()) {
            response.getWriter().println("Chapter ID or number of questions is invalid.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        if (examId != null) {
            response.getWriter().println("Đang có 1 bài kiểm tra khác, vui lòng quay về làm");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        int numQuestions = Integer.parseInt(numQuestionsParam);
        Long studentId = Long.parseLong(studentIdParam);
        Long chapterId = Long.parseLong(chapterIdParam);
        int difficulty = 1;
        switch (difficultyParam) {
            case "ai":
                difficulty = levelService.findByStudentIdAndChapterId(studentId, chapterId).getLevel();
                break;
            case "veryEasy":
                difficulty = 1;
                break;
            case "easy":
                difficulty = 2;
                break;
            case "medium":
                difficulty = 3;
                break;
            case "hard":
                difficulty = 4;
                break;
            case "veryHard":
                difficulty = 5;
                break;
        }
        List<QuestionResponse> questionList = examService.getQuestionsForExam(numQuestions, chapterId, difficulty, studentId);
        request.setAttribute("questionList", questionList);
    }

}
