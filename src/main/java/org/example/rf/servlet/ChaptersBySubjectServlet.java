package org.example.rf.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.dao.ChapterDAO;
import org.example.rf.model.Chapter;
import org.example.rf.service.ChapterService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/chapters-by-course")
public class ChaptersBySubjectServlet extends HttpServlet {

    private ChapterService chapterService = new ChapterService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String courseIdParam = request.getParameter("courseId");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Long courseId = Long.parseLong(courseIdParam);
        if (courseIdParam == null || courseIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Missing subjectId parameter\"}");
            return;
        }

        List<Chapter> chapters = chapterService.getAllChaptersByCourseId(courseId);

        JSONArray jsonArray = new JSONArray();
        for (Chapter chapter : chapters) {
            JSONObject obj = new JSONObject();
            obj.put("id", chapter.getId());
            obj.put("title", chapter.getTitle());
            jsonArray.put(obj);
        }

        out.write(jsonArray.toString());
        out.flush();
    }
}
