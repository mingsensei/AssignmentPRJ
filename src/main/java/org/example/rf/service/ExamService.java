package org.example.rf.service;

import org.example.rf.api.python.PythonApiClient;
import org.example.rf.dao.*;
import org.example.rf.dto.AnswerCheckDTO;
import org.example.rf.dto.QuestionRequestPython;
import org.example.rf.dto.QuestionResponse;
import org.example.rf.model.*;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExamService {

    private final EntityManager em;
    private final ExamDAO examDAO;
    private final QuestionDAO questionDAO;
    private final AiQuestionDAO aiQuestionDAO;
    private final ExamQuestionDAO examQuestionDAO;
    private final MaterialDAO materialDAO;
    private final PythonApiClient pythonApiClient;
    private final LevelDAO levelDAO;
    private final TestAttemptService testAttemptService;
    private final UserSubscriptionService subscriptionService;
    private final PlanService planService;

    public ExamService() {
        this.pythonApiClient = new PythonApiClient();
        this.em = JPAUtil.getEntityManager();  // Tạo EntityManager 1 lần
        this.examDAO = new ExamDAO(em);        // Tạo DAO 1 lần dùng chung EntityManager
        this.questionDAO = new QuestionDAO(em);
        this.aiQuestionDAO = new AiQuestionDAO(em);
        this.examQuestionDAO = new ExamQuestionDAO(em);
        this.materialDAO = new MaterialDAO(em);
        this.levelDAO = new LevelDAO(em);
        this.testAttemptService = new TestAttemptService();
        this.subscriptionService = new UserSubscriptionService();
        this.planService = new PlanService();

    }

    public void createExam(Exam exam) {
        examDAO.create(exam);
    }

    public Exam getExamById(Long id) {
        return examDAO.findById(id);
    }

    public void updateExam(Exam exam) {
        examDAO.update(exam);
    }

    public void deleteExam(Long id) {
        examDAO.delete(id);
    }

    public List<Exam> getAllExams() {
        return examDAO.findAll();
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    public List<QuestionResponse> getQuestionsForExam(int numQuestions, Long chapterId, int difficulty, Long studentId, Exam exam) throws IOException {
        int firstIndexOrderQuestion = 1;
        List<Question> questionList = questionDAO.findAllByChapterIdAndDifficulty(exam.getId(), difficulty);
        AtomicInteger order = new AtomicInteger(firstIndexOrderQuestion);
        questionList.stream()
                .map(question -> ExamQuestion.builder()
                        .questionId(question.getId())
                        .examId(exam.getId())
                        .questionOrder(order.getAndIncrement())
                        .build())
                .forEach(examQuestionDAO::create);

        List<AiQuestion> aiQuestionList = new ArrayList<>();
        int numAiQuestions = numQuestions - questionList.size();
        if(questionList.isEmpty() || numAiQuestions > 0) {
            aiQuestionList = getGeneratedAiQuestions(chapterId, numAiQuestions, difficulty);
            if(aiQuestionList == null) {
                return null;
            }
        }

        aiQuestionList.forEach(aiQuestionDAO::create);
        aiQuestionList.stream()
                .map(aiQuestion -> ExamQuestion.builder()
                        .aiQuestionId(aiQuestion.getId())
                        .examId(exam.getId())
                        .questionOrder(order.getAndIncrement())
                        .build())
                .forEach(examQuestionDAO::create);


        List<QuestionResponse> questionResponseList = new ArrayList<>();
        for (AiQuestion aiQuestion : aiQuestionList) {
            QuestionResponse questionResponse = QuestionResponse.builder()
                    .id(aiQuestion.getId())
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
                    .id(question.getId())
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

    public Exam createNewExam(Long chapterId, Long studentId) {
        // Lấy subscription đang active
        UserSubscription subscription = subscriptionService
                .getAllSubscriptions().stream()
                .filter(s -> s.getUserId().equals(studentId) &&
                        (s.getEndDate() == null || s.getEndDate().isAfter(LocalDate.now())))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User không có gói dịch vụ hợp lệ"));

        // Lấy plan tương ứng
        Plan plan = planService.getPlanById(subscription.getPlanId());

        // Đếm số lần đã làm bài kiểm tra
        long currentAttempts = testAttemptService
                .getAllTestAttempts().stream()
                .filter(attempt -> attempt.getUserId().equals(studentId))
                .count();

        if (currentAttempts >= plan.getMaxTestAttempts()) {
            throw new RuntimeException("Bạn đã đạt giới hạn làm bài kiểm tra của gói hiện tại");
        }

        // Cho phép tạo Exam
        Exam exam = Exam.builder()
                .studentId(studentId)
                .chapterId(chapterId)
                .build();
        examDAO.create(exam);

        // Ghi nhận lượt attempt
        TestAttempt testAttempt = TestAttempt.builder()
                .userId(studentId)
                .testId(exam.getId()) // hoặc examId nếu test = exam
                .attemptedAt(LocalDateTime.now())
                .build();
        testAttemptService.createTestAttempt(testAttempt);

        return exam;
    }


    public List<QuestionResponse> addMoreQuestionForExam(Long examId, int additionalQuestions, Long studentId, Long chapterId) throws IOException {
        int levelValue = updateLevelOfStudent(studentId, chapterId, examId);
        List<QuestionRequestPython> questionRequestPythons = aiQuestionDAO.findAnsweredQuestionData(examId);
        int firstIndexOrderQuestion = questionRequestPythons.size() + 1;

        List<AiQuestion> aiQuestionList = getGeneratedAiQuestions(chapterId, additionalQuestions, levelValue);
        aiQuestionList.forEach(aiQuestionDAO::create);

        AtomicInteger order = new AtomicInteger(firstIndexOrderQuestion);
        aiQuestionList.stream()
                .map(aiQuestion -> ExamQuestion.builder()
                        .aiQuestionId(aiQuestion.getId())
                        .examId(examId)
                        .questionOrder(order.getAndIncrement())
                        .build())
                .forEach(examQuestionDAO::create);

        List<QuestionResponse> questionResponseList = new ArrayList<>();
        for (AiQuestion aiQuestion : aiQuestionList) {
            QuestionResponse questionResponse = QuestionResponse.builder()
                    .id(aiQuestion.getId())
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
        return questionResponseList;
    }

    public int updateLevelOfStudent(Long studentId, Long chapterId, Long examId) throws IOException {
        List<QuestionRequestPython> questionRequestPythons = aiQuestionDAO.findAnsweredQuestionData(examId);
        int firstIndexOrderQuestion = questionRequestPythons.size();
        JSONArray questionsData = new JSONArray();
        for (QuestionRequestPython question : questionRequestPythons) {
            JSONObject questionData = new JSONObject();
            questionData.put("difficulty", question.getDifficulty());
            questionData.put("isCorrect", question.getStudentAnswer().equals(question.getCorrectAnswer()));
            questionsData.put(questionData);
        }

        String thetaJson = pythonApiClient.callPythonAPIForTheta(questionsData.toString());
        JSONObject thetaResponse = new JSONObject(thetaJson);
        int levelValue = thetaResponse.getInt("level");

        Level existingLevel = levelDAO.findByStudentIdAndChapterId(studentId, chapterId);
        if (existingLevel != null) {
            existingLevel.setLevel(levelValue);
            levelDAO.update(existingLevel);
        } else {
            Level level = new Level();
            level.setStudentId(studentId);
            level.setChapterId(chapterId);
            level.setLevel(levelValue);
            levelDAO.create(level);
        }
        return levelValue;
    }

    private List<AiQuestion> getGeneratedAiQuestions(Long chapterId, int numAiQuestions, int difficulty) throws IOException {
        List<Material> materialList =  materialDAO.findAllByChapterId(chapterId);
        if(materialList.isEmpty()) {
            return null;
        }
//            List<String> vectorDbPathList = new ArrayList<>();
//            for (Material material : materialList) {
//                vectorDbPathList.add(material.getVectorDbPath());
//            }
        String vectorDbPath = materialList.get(0).getVectorDbPath();
        return pythonApiClient.generateQuestion(numAiQuestions, difficulty, vectorDbPath);
    }

    public int getResultByExamIdAndStudentId(Long examId) {
        List<AnswerCheckDTO> answerCheckDTOList = examDAO.findAnswerChecks(examId);
        int resultCount = 0;
        for (AnswerCheckDTO answerCheckDTO : answerCheckDTOList) {
            if(answerCheckDTO.getStudentAnswer().equals(answerCheckDTO.getCorrectOption())) {
                resultCount++;
            }
        };
        return resultCount;
    }

    public int getSizeByExamId(Long examId) {
        List<ExamQuestion> examQuestionList = examQuestionDAO.findByExamId(examId);
        return examQuestionList.size();
    }
}
