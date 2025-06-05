package org.example.rf.service;

import org.example.rf.api.python.PythonApiClient;
import org.example.rf.dao.*;
import org.example.rf.dto.QuestionResponse;
import org.example.rf.model.*;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExamService {
    int firstIndexOrderQuestion = 1;
    private final EntityManager em;
    private final ExamDAO examDAO;
    private final QuestionDAO questionDAO;
    private final AiQuestionDAO aiQuestionDAO;
    private final ExamQuestionDAO examQuestionDAO;
    private final MaterialDAO materialDAO;
    private final PythonApiClient pythonApiClient;
    public ExamService() {
        this.pythonApiClient = new PythonApiClient();
        this.em = JPAUtil.getEntityManager();  // Tạo EntityManager 1 lần
        this.examDAO = new ExamDAO(em);        // Tạo DAO 1 lần dùng chung EntityManager
        this.questionDAO = new QuestionDAO(em);
        this.aiQuestionDAO = new AiQuestionDAO(em);
        this.examQuestionDAO = new ExamQuestionDAO(em);
        this.materialDAO = new MaterialDAO(em);
    }

    // Tạo mới Exam
    public void createExam(Exam exam) {
        examDAO.create(exam);
    }

    // Tìm Exam theo ID
    public Exam getExamById(Long id) {
        return examDAO.findById(id);
    }

    // Cập nhật Exam
    public void updateExam(Exam exam) {
        examDAO.update(exam);
    }

    // Xóa Exam theo ID
    public void deleteExam(Long id) {
        examDAO.delete(id);
    }

    // Lấy danh sách tất cả Exam
    public List<Exam> getAllExams() {
        return examDAO.findAll();
    }

    // Đóng EntityManager khi không còn dùng
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    public List<QuestionResponse> getQuestionsForExam(int numQuestions, Long chapterId, int difficulty, Long studentId) throws IOException {
        Exam exam = Exam.builder()
                .studentId(studentId)
                .chapterId(chapterId)
                .build();

        List<Question> questionList = questionDAO.findAllByChapterIdAndDifficulty(exam.getId(), numQuestions);

        for (Question question : questionList) {
            ExamQuestion examQuestion = ExamQuestion.builder()
                    .questionId(question.getId())
                    .examId(exam.getId())
                    .questionOrder(firstIndexOrderQuestion)
                    .build();
            examQuestionDAO.create(examQuestion);
            firstIndexOrderQuestion++;
        }
        List<AiQuestion> aiQuestionList = new ArrayList<>();

        if(questionList.isEmpty() || questionList.size() < numQuestions) {
            int numAiQuestions = numQuestions - questionList.size();
            List<Material> materialList =  materialDAO.findAllByChapterId(chapterId);
//            List<String> vectorDbPathList = new ArrayList<>();
//            for (Material material : materialList) {
//                vectorDbPathList.add(material.getVectorDbPath());
//            }
            String vectorDbPath = materialList.get(0).getVectorDbPath();
            aiQuestionList = pythonApiClient.generateQuestion(numAiQuestions, difficulty, vectorDbPath);
        }

        List<QuestionResponse> questionResponseList = new ArrayList<>();
        for (AiQuestion aiQuestion : aiQuestionList) {
            QuestionResponse questionResponse = QuestionResponse.builder()
                    .content(aiQuestion.getContent())
                    .difficulty(aiQuestion.getDifficulty())
                    .optionA(aiQuestion.getOptionA())
                    .optionB(aiQuestion.getOptionB())
                    .optionC(aiQuestion.getOptionC())
                    .optionD(aiQuestion.getOptionD())
                    .correctOption(aiQuestion.getCorrectOption())
                    .explain(aiQuestion.getExplain())
                    .build();
            questionResponseList.add(questionResponse);
        }
        for (Question question : questionList) {
            QuestionResponse questionResponse = QuestionResponse.builder()
                    .content(question.getContent())
                    .difficulty(question.getDifficulty())
                    .optionA(question.getOptionA())
                    .optionB(question.getOptionB())
                    .optionC(question.getOptionC())
                    .optionD(question.getOptionD())
                    .correctOption(question.getCorrectOption())
                    .explain(question.getExplain())
                    .build();
        }
        return questionResponseList;
    }

}
