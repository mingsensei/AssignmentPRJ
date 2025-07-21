package org.example.rf.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rf.model.AiQuestion;
import org.example.rf.model.Question;
import org.example.rf.model.Course;
import org.example.rf.model.Chapter;
import org.example.rf.service.AiQuestionService;
import org.example.rf.service.QuestionService;
import org.example.rf.service.CourseService;
import org.example.rf.service.ChapterService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/question/*")
public class AdminQuestionServlet extends HttpServlet {
    private final QuestionService questionService = new QuestionService();
    private final AiQuestionService aiQuestionService = new AiQuestionService();
    private final CourseService courseService = new CourseService();
    private final ChapterService chapterService = new ChapterService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // Lấy danh sách dữ liệu
            List<Question> questions = questionService.getAllQuestions();
            List<AiQuestion> aiQuestions = aiQuestionService.getAllAiQuestions();
            List<Integer> questionDifficulties = questionService.getAllDifficulties();
            List<Integer> aiQuestionDifficulties = aiQuestionService.getAllDifficulties();
            List<Course> courses = courseService.getAllCourses();

            // Đặt vào request attributes
            request.setAttribute("questions", questions);
            request.setAttribute("aiQuestions", aiQuestions);
            request.setAttribute("questionDifficulties", questionDifficulties);
            request.setAttribute("aiQuestionDifficulties", aiQuestionDifficulties);
            request.setAttribute("courses", courses);

            // Chuyển hướng đến JSP
            request.getRequestDispatcher("/view/admin/question.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
            if (path != null) {
                switch (path) {
                    case "/add":
                        addNewQuestion(request, response);
                        break;
                    case "/addFromAiQuestion":
                        addNewQuestionFromAiQuestion(request, response);
                        break;
                    case "/update":
                        updateQuestion(request, response);
                        break;
                    case "/update_ai_question":
                        updateAiQuestion(request, response);
                        break;
                    case "/delete":
                        deleteQuestion(request, response);
                        break;
                    default:
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Hành động không được hỗ trợ");
                        break;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không có hành động được chỉ định");
            }
    }

    private void addNewQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] contents = request.getParameterValues("content[]");
        String[] optionAs = request.getParameterValues("optionA[]");
        String[] optionBs = request.getParameterValues("optionB[]");
        String[] optionCs = request.getParameterValues("optionC[]");
        String[] optionDs = request.getParameterValues("optionD[]");
        String[] correctOptions = request.getParameterValues("correctOption[]");
        String[] difficulties = request.getParameterValues("difficulty[]");
        String[] courseIds = request.getParameterValues("courseId[]");
        String[] chapterIds = request.getParameterValues("chapterId[]");
        String[] explains = request.getParameterValues("explain[]");

        if (contents == null || contents.length == 0 || courseIds == null || chapterIds == null) {
            request.setAttribute("errorMessage", "Không có câu hỏi hoặc thông tin môn học/chương không hợp lệ!");
            doGet(request, response);
            return;
        }

        List<Question> newQuestions = new ArrayList<>();
        for (int i = 0; i < contents.length; i++) {
            try {
                // Xác thực dữ liệu
                if (contents[i].isEmpty() || optionAs[i].isEmpty() || optionBs[i].isEmpty() ||
                        optionCs[i].isEmpty() || optionDs[i].isEmpty() || correctOptions[i].isEmpty() ||
                        difficulties[i].isEmpty() || courseIds[i].isEmpty() || chapterIds[i].isEmpty()) {
                    request.setAttribute("errorMessage", "Dữ liệu không đầy đủ cho câu hỏi #" + (i + 1));
                    doGet(request, response);
                    return;
                }

                Question question = new Question();
                question.setContent(contents[i]);
                question.setOptionA(optionAs[i]);
                question.setOptionB(optionBs[i]);
                question.setOptionC(optionCs[i]);
                question.setOptionD(optionDs[i]);
                question.setCorrectOption(correctOptions[i]);
                question.setDifficulty(Integer.parseInt(difficulties[i]));
                question.setChapterId(Long.parseLong(chapterIds[i]));
                question.setExplain(explains[i] != null ? explains[i] : "");

                // Kiểm tra xem chapterId có thuộc courseId không
                Chapter chapter = chapterService.getChapterById(Long.parseLong(chapterIds[i]));
                if (chapter == null || !chapter.getCourseId().equals(Long.parseLong(courseIds[i]))) {
                    request.setAttribute("errorMessage", "Chương không hợp lệ hoặc không thuộc môn học cho câu hỏi #" + (i + 1));
                    doGet(request, response);
                    return;
                }

                newQuestions.add(question);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Dữ liệu không hợp lệ cho câu hỏi #" + (i + 1));
                doGet(request, response);
                return;
            }
        }

        try {
            for (Question question : newQuestions) {
                questionService.createQuestion(question);
            }
            request.setAttribute("successMessage", "Thêm câu hỏi thành công!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi khi lưu câu hỏi: " + e.getMessage());
        }
        doGet(request, response);
    }

    private void addNewQuestionFromAiQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] selectedIds = request.getParameterValues("selectedAIQuestions");
        String targetChapterIdStr = request.getParameter("targetChapterId");

        if (selectedIds == null || selectedIds.length == 0) {
            request.setAttribute("errorMessage", "Vui lòng chọn ít nhất một câu hỏi AI!");
            doGet(request, response);
            return;
        }

        if (targetChapterIdStr == null || targetChapterIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng chọn chương đích!");
            doGet(request, response);
            return;
        }

        try {
            Long targetChapterId = Long.parseLong(targetChapterIdStr);
            Chapter chapter = chapterService.getChapterById(targetChapterId);
            if (chapter == null) {
                request.setAttribute("errorMessage", "Chương không tồn tại!");
                doGet(request, response);
                return;
            }
            Long targetCourseId = chapter.getCourseId();

            for (String id : selectedIds) {
                Long aiQuestionId = Long.parseLong(id);
                AiQuestion aiQuestion = aiQuestionService.getAiQuestionById(aiQuestionId);
                if (aiQuestion != null) {
                    Question question = new Question();
                    question.setContent(aiQuestion.getContent());
                    question.setOptionA(aiQuestion.getOptionA());
                    question.setOptionB(aiQuestion.getOptionB());
                    question.setOptionC(aiQuestion.getOptionC());
                    question.setOptionD(aiQuestion.getOptionD());
                    question.setCorrectOption(aiQuestion.getCorrectOption());
                    question.setDifficulty(aiQuestion.getDifficulty());
                    question.setChapterId(targetChapterId);
                    question.setExplain(aiQuestion.getExplain());
                    questionService.createQuestion(question);
                }
            }
            request.setAttribute("successMessage", "Chuyển đổi câu hỏi AI thành công!");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi khi chuyển đổi câu hỏi: " + e.getMessage());
        }
        doGet(request, response);
    }

    private void updateQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long questionId = Long.parseLong(request.getParameter("id"));
            Question question = questionService.getQuestionById(questionId);
            if (question == null) {
                request.setAttribute("errorMessage", "Câu hỏi không tồn tại!");
                doGet(request, response);
                return;
            }

            String content = request.getParameter("content");
            String optionA = request.getParameter("optionA");
            String optionB = request.getParameter("optionB");
            String optionC = request.getParameter("optionC");
            String optionD = request.getParameter("optionD");
            String correctOption = request.getParameter("correctOption");
            String difficultyStr = request.getParameter("difficulty");
            String chapterIdStr = request.getParameter("chapterId");
            String explain = request.getParameter("explain");

            // Xác thực dữ liệu
            if (content == null || content.isEmpty() || optionA == null || optionA.isEmpty() ||
                    optionB == null || optionB.isEmpty() || optionC == null || optionC.isEmpty() ||
                    optionD == null || optionD.isEmpty() || correctOption == null || correctOption.isEmpty() ||
                    difficultyStr == null || difficultyStr.isEmpty() || chapterIdStr == null || chapterIdStr.isEmpty()) {
                request.setAttribute("errorMessage", "Dữ liệu không đầy đủ!");
                doGet(request, response);
                return;
            }

            int difficulty = Integer.parseInt(difficultyStr);
            Long chapterId = Long.parseLong(chapterIdStr);
            Chapter chapter = chapterService.getChapterById(chapterId);
            if (chapter == null) {
                request.setAttribute("errorMessage", "Chương không tồn tại!");
                doGet(request, response);
                return;
            }

            question.setContent(content);
            question.setOptionA(optionA);
            question.setOptionB(optionB);
            question.setOptionC(optionC);
            question.setOptionD(optionD);
            question.setCorrectOption(correctOption);
            question.setDifficulty(difficulty);
            question.setChapterId(chapterId);
            question.setExplain(explain != null ? explain : "");

            questionService.updateQuestion(question);
            request.setAttribute("successMessage", "Cập nhật câu hỏi thành công!");
            doGet(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ!");
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi khi cập nhật câu hỏi: " + e.getMessage());
            doGet(request, response);
        }
    }

    private void updateAiQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long aiQuestionId = Long.parseLong(request.getParameter("ai_question_id"));
            AiQuestion aiQuestion = aiQuestionService.getAiQuestionById(aiQuestionId);
            if (aiQuestion == null) {
                request.setAttribute("errorMessage", "Câu hỏi AI không tồn tại!");
                doGet(request, response);
                return;
            }

            String content = request.getParameter("content");
            String optionA = request.getParameter("optionA");
            String optionB = request.getParameter("optionB");
            String optionC = request.getParameter("optionC");
            String optionD = request.getParameter("optionD");
            String correctOption = request.getParameter("correctOption");
            String difficultyStr = request.getParameter("difficulty");
            String explain = request.getParameter("explain");

            // Xác thực dữ liệu
            if (content == null || content.isEmpty() || optionA == null || optionA.isEmpty() ||
                    optionB == null || optionB.isEmpty() || optionC == null || optionC.isEmpty() ||
                    optionD == null || optionD.isEmpty() || correctOption == null || correctOption.isEmpty() ||
                    difficultyStr == null || difficultyStr.isEmpty()) {
                request.setAttribute("errorMessage", "Dữ liệu không đầy đủ!");
                doGet(request, response);
                return;
            }

            int difficulty = Integer.parseInt(difficultyStr);

            aiQuestion.setContent(content);
            aiQuestion.setOptionA(optionA);
            aiQuestion.setOptionB(optionB);
            aiQuestion.setOptionC(optionC);
            aiQuestion.setOptionD(optionD);
            aiQuestion.setCorrectOption(correctOption);
            aiQuestion.setDifficulty(difficulty);
            aiQuestion.setExplain(explain != null ? explain : "");

            aiQuestionService.updateAiQuestion(aiQuestion);
            request.setAttribute("successMessage", "Cập nhật câu hỏi AI thành công!");
            doGet(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ!");
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi khi cập nhật câu hỏi AI: " + e.getMessage());
            doGet(request, response);
        }
    }

    private void deleteQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String type = request.getParameter("type");
            Long id = Long.parseLong(request.getParameter("id"));

            if ("normal".equals(type)) {
                Question question = questionService.getQuestionById(id);
                if (question == null) {
                    request.setAttribute("errorMessage", "Câu hỏi không tồn tại!");
                } else {
                    questionService.deleteQuestion(id);
                    request.setAttribute("successMessage", "Xóa câu hỏi thành công!");
                }
            } else if ("ai".equals(type)) {
                AiQuestion aiQuestion = aiQuestionService.getAiQuestionById(id);
                if (aiQuestion == null) {
                    request.setAttribute("errorMessage", "Câu hỏi AI không tồn tại!");
                } else {
                    aiQuestionService.deleteAiQuestion(id);
                    request.setAttribute("successMessage", "Xóa câu hỏi AI thành công!");
                }
            } else {
                request.setAttribute("errorMessage", "Loại câu hỏi không hợp lệ!");
            }
            doGet(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID không hợp lệ!");
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi khi xóa câu hỏi: " + e.getMessage());
            doGet(request, response);
        }
    }
}