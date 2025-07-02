//package org.example.rf.servlet.admin;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.example.rf.model.AiQuestion;
//import org.example.rf.model.Question;
//import org.example.rf.service.AiQuestionService;
//import org.example.rf.service.QuestionService;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@WebServlet("/admin/question")
//public class AdminQuestionServlet extends HttpServlet {
//    private final QuestionService questionService = new QuestionService();
//    private final AiQuestionService aiQuestionService = new AiQuestionService();
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<Question> questions = questionService.getAllQuestions();
//        request.setAttribute("questions", questions);
//        List<AiQuestion> aiQuestions = aiQuestionService.getAllAiQuestions();
//        request.setAttribute("aiQuestions", aiQuestions);
//        request.getRequestDispatcher("/view/admin/question.jsp").forward(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String path = request.getPathInfo();
//        if(path.equals("add")) {
//            addNewQuestion(request, response);
//        } else if(path.equals("add_from_aiQuestion")) {
//            addNewQuestionFromAiQuestion(request, response);
//        } else if(path.equals("update_question")) {
//            updateQuestion(request, response);
//        } else if(path.equals("update_aiQuestion")) {
//            updateAiQuestion(request, response);
//        } else if(path.equals("delete_question")) {
//            deleteQuestion(request, response);
//        } else if(path.equals("delete_aiQuestion")) {
//            deleteAiQuestion(request, response);
//        }
//    }
//
//    private void addNewQuestionFromAiQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String[] selectedIds = request.getParameterValues("selectedAiQuestionIds");
//
//        if (selectedIds == null || selectedIds.length == 0) {
//            request.setAttribute("errorMessage", "Vui lòng chọn ít nhất một câu hỏi!");
//            doGet(request, response);
//            return;
//        }
//
//        for (String id : selectedIds) {
//            AiQuestion aiQuestion = aiQuestionService.
//        }
//
//    }
//
//    private void addNewQuestion(HttpServletRequest request, HttpServletResponse response) {
//
//    }
//
//    private void updateQuestion(HttpServletRequest request, HttpServletResponse response) {
//
//    }
//
//    private void updateAiQuestion(HttpServletRequest request, HttpServletResponse response) {
//
//    }
//
//    private void deleteQuestion(HttpServletRequest request, HttpServletResponse response) {
//
//    }
//
//    private void deleteAiQuestion(HttpServletRequest request, HttpServletResponse response) {
//
//    }
//}
