package org.example.rf.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.rf.model.Category;
import org.example.rf.model.Course;
import org.example.rf.service.CategoryService;
import org.example.rf.service.CourseService;
import org.example.rf.service.GeminiKeywordExtractor;

import java.io.*;
import java.util.*;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private CourseService courseService;

    @Override
    public void init() throws ServletException {
        courseService = new CourseService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String query = req.getParameter("q");
        List<Course> allCourses = courseService.getAllCourses();
        List<Course> found = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            List<String> keywords = extractKeywords(query); // Bạn có thể thay bằng gọi LLM ở đây
            for (Course c : allCourses) {
                for (String kw : keywords) {
                    if (c.getDescription().toLowerCase().contains(kw.toLowerCase()) ||
                            c.getName().toLowerCase().contains(kw.toLowerCase())) {
                        found.add(c);
                        break;
                    }
                }
            }
        }

        CategoryService categoryService = new CategoryService();
        List<Category> categoryList = categoryService.getAllCategories(); // Lấy dữ liệu từ DB
        req.setAttribute("categoryList", categoryList);
        req.setAttribute("url", req.getRequestURL().toString());
        req.setAttribute("query", query);
        req.setAttribute("courses", found);
        req.getRequestDispatcher("category.jsp").forward(req, resp);
    }

    private List<String> extractKeywords(String input) {
        List<String> extractedKeywords = new ArrayList<>();
        GeminiKeywordExtractor extractor = new GeminiKeywordExtractor();
        // Demo: tách các từ dài hơn 2 ký tự, bạn thay bằng LLM thực tế nếu muốn
        try {
            extractedKeywords = extractor.extractKeywordsFromGemini(input);
            System.out.println("Từ khóa được trích xuất bởi Gemini: " + extractedKeywords);
            // Kết quả mong đợi (có thể thay đổi tùy thuộc vào Gemini):
            // [Lập trình Java, kỹ năng quan trọng, phát triển phần mềm, Học thuật toán, tư duy logic, giải quyết vấn đề]
        } catch (IOException e) {
            System.err.println("Lỗi khi gọi Gemini API: " + e.getMessage());
            e.printStackTrace();
        }
        return extractedKeywords;
    }
}
