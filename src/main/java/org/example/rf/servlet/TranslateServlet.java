package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.rf.service.GeminiKeywordExtractor;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ai-translate")
public class TranslateServlet extends HttpServlet {

    // Hiện form dịch khi GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Forward tới file JSP giao diện (ví dụ: /WEB-INF/views/ai-translate.jsp)
        req.getRequestDispatcher("translate.jsp").forward(req, resp);

        // Nếu muốn render HTML trực tiếp (không dùng JSP), thay thế bằng:
        // resp.setContentType("text/html; charset=UTF-8");
        // PrintWriter out = resp.getWriter();
        // out.println("...toàn bộ code HTML giao diện ở đây...");
    }

    // Xử lý khi POST (dịch)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String text = req.getParameter("text");
        String from = req.getParameter("from");
        String to = req.getParameter("to");

        String result;
        if (text == null || text.isBlank()) {
            result = "";
        } else {
            GeminiKeywordExtractor geminiKeywordExtractor = new GeminiKeywordExtractor();

            result = geminiKeywordExtractor.translateSmartly(text, from, to);
        }

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain");
        resp.getWriter().write(result);
    }
}

