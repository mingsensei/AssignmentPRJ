package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.rf.dao.QuestionResult;
import org.example.rf.dto.QuestionResponse;
import org.example.rf.model.*;
import org.example.rf.service.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/exam/*")
public class ExamServlet extends HttpServlet {

    private final ExamService examService = new ExamService();
    private final LevelService levelService = new LevelService();
    private final ExamQuestionService examQuestionService = new ExamQuestionService();
    private final QuestionService questionService = new QuestionService();
    private final ChapterService chapterService = new ChapterService();
    private final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            handleExamSetupGet(request, response);
        } else if (pathInfo.equals("/setup")) {
            handleExamSetupGet(request, response);
        } else if (pathInfo.equals("/setup/demo")) {
            handleExamSetupDemoGet(request, response);
        } else if (pathInfo.equals("/history")) {
            handleExamHistoryGet(request, response);
        } else if(pathInfo.equals("/review")) {
            handleExamHistoryGet(request, response);
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
        } else if(pathInfo.equals("/submit")) {
            handleExamSubmit(request, response);
        } else if (pathInfo.equals("/addMore")) {
            handleAddMoreQuestionPost(request, response);
        }
    }

    private void handleExamSetupGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String chapterId = request.getParameter("chapterId");
        request.setAttribute("chapterId", chapterId);
        request.getRequestDispatcher("/setupExam.jsp").forward(request, response);
    }

    private void handleExamSetupDemoGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Course> courseList = courseService.getAllCourses();
        List<Chapter> chapterList = chapterService.getAllChapters();
        request.setAttribute("courses", courseList);
        request.setAttribute("chapters", chapterList);
        request.getRequestDispatcher("/examSetupDemo.jsp").forward(request, response);
    }

    private void handleExamPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = (User) request.getSession().getAttribute("user");
        Long studentId = user.getId();
        String numQuestionsParam = request.getParameter("numQuestionsParam");
        String chapterIdParam = request.getParameter("chapterId");
        String difficultyParam = request.getParameter("difficulty");
        String examId = (String) request.getSession().getAttribute("examId");

        if (chapterIdParam == null || chapterIdParam.isEmpty()) {
            response.getWriter().println("Chapter ID or number of questions is invalid.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        //O day phai check time -> vao history de xem list exam -> check thay co 1 cai chua submitted thi chon continue
        if (examId != null) {
            response.getWriter().println("Đang có 1 bài kiểm tra khác, vui lòng quay về làm");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        try {
            int numQuestions = Integer.parseInt(numQuestionsParam);
            Long chapterId = Long.parseLong(chapterIdParam);
            int difficulty = convertDifficulty(difficultyParam, studentId, chapterId);

            // Gọi ExamService: sẽ kiểm tra quota và ném lỗi nếu đã hết giới hạn
            Exam exam = examService.createNewExam(chapterId, studentId);
            request.getSession().setAttribute("examId", exam.getId().toString());

            List<QuestionResponse> questionList = examService.getQuestionsForExam(numQuestions, chapterId, difficulty, studentId, exam);
            if (questionList == null || questionList.isEmpty()) {
                request.getSession().removeAttribute("examId");
                request.setAttribute("errorMessage", "Không tìm thấy tài liệu hoặc không thể tạo câu hỏi.");
                request.getRequestDispatcher("/setupExam.jsp").forward(request, response);
                return;
            }

            request.setAttribute("questionList", questionList);
            request.getRequestDispatcher("/exam.jsp").forward(request, response);

        } catch (RuntimeException e) {
            // Lỗi vượt quota hoặc logic khác từ createNewExam
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/setupExam.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Lỗi hệ thống khi tạo bài kiểm tra: " + e.getMessage());
        }
    }

    private void handleExamSubmit(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String examIdStr = (String) request.getSession().getAttribute("examId");
        User user = (User) request.getSession().getAttribute("user");
        Long studentId = user.getId();
        if (examIdStr == null) {
            response.getWriter().println("No active exam found.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Long examId = Long.parseLong(examIdStr);

            Map<String, String> studentAnswers = extractStudentAnswers(request);

            examQuestionService.saveStudentAnswers(examId, studentAnswers);
            Exam exam = examService.getExamById(examId);
            exam.setSubmittedAt(LocalDateTime.now());
            examService.updateExam(exam);
            int level = examService.updateLevelOfStudent(studentId, exam.getChapterId(), examId);
            request.getSession().removeAttribute("examId");

            response.sendRedirect(request.getContextPath() + "/review?examid=" + examId);

        } catch (Exception e) {
            response.getWriter().println("Error submitting exam: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, String> extractStudentAnswers(HttpServletRequest request) {
        Map<String, String> answers = new HashMap<>();

        for (String paramName : request.getParameterMap().keySet()) {
            if (paramName.startsWith("answer_")) {
                String questionId = paramName.substring(7);
                String answer = request.getParameter(paramName);
                if (answer != null && !answer.trim().isEmpty()) {
                    answers.put(questionId, answer);
                }
            }
        }

        return answers;
    }

    private void handleAddMoreQuestionPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String additionalQuestionsParam = request.getParameter("additionalQuestions");
        int additionalQuestions = Integer.parseInt(additionalQuestionsParam);
        String examIdParam = (String) request.getSession().getAttribute("examId");
        Long examId = Long.parseLong(examIdParam);
        User user = (User) request.getSession().getAttribute("user");

        Map<String, String> studentAnswers = extractStudentAnswers(request);
        examQuestionService.saveStudentAnswers(examId, studentAnswers);

        Exam exam = examService.getExamById(examId);
        Long studentId = user.getId();
        List<QuestionResponse> questionResponseList = examService.addMoreQuestionForExam(examId, additionalQuestions, studentId, exam.getChapterId());
        request.setAttribute("questionList", questionResponseList);
        request.getRequestDispatcher("/exam.jsp").forward(request, response);
    }

    private int convertDifficulty(String difficultyParam, Long studentId, Long chapterId) {
        Level level = levelService.findByStudentIdAndChapterId(studentId, chapterId);
        int difficulty = -1;
        switch (difficultyParam) {
            case "ai":
                if(level == null) {
                    difficulty = 1;
                }else {
                    difficulty = level.getLevel();
                }
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
        return difficulty;
    }

    private void handleExamHistoryGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        Long studentId = user.getId();

        List<Exam> examHistory = examService.getExamsByStudentId(studentId);

        request.setAttribute("examHistory", examHistory);

        request.getRequestDispatcher("/examHistory.jsp").forward(request, response);
    }

    private void handleReviewExam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long examId = Long.valueOf(request.getParameter("examid"));
        List<QuestionResult> results = examService.getExamQuestions(examId);
        request.setAttribute("results", results);
        request.getRequestDispatcher("/review.jsp").forward(request, response);
    }

}
