package org.example.rf.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.example.rf.dao.QuestionResult;
import org.example.rf.service.ExamService;

@WebServlet(name = "ReviewServlet", urlPatterns = {"/Review"})

public class ReviewServlet extends HttpServlet {
    private ExamService examService;

    @Override
    public void init() throws ServletException {
        examService = new ExamService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long examId = Long.valueOf(request.getParameter("examid"));
        List<QuestionResult> results = examService.getExamQuestions(examId);
        request.setAttribute("results", results);
        request.getRequestDispatcher("/review.jsp").forward(request, response);
    }

}
