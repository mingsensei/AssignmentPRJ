package org.example.rf.service;

import org.example.rf.dao.QuestionResult;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        this.em = JPAUtil.getEntityManager();
        this.examDAO = new ExamDAO(em);
        this.questionDAO = new QuestionDAO(em);
        this.aiQuestionDAO = new AiQuestionDAO(em);
        this.examQuestionDAO = new ExamQuestionDAO(em);
        this.materialDAO = new MaterialDAO();
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

    public List<QuestionResponse> getQuestionsForExam(int numQuestions, Long chapterId, int difficulty, Long studentId, Exam exam) throws Exception {
        int firstIndexOrderQuestion = 1;

        List<Question> allQuestionList = questionDAO.findAllByChapterIdAndDifficulty(exam.getChapterId(), difficulty);
        List<Question> questionList = new ArrayList<>();

        int countQuestion = numQuestions;
        for(Question question : allQuestionList) {
            if(countQuestion == 0) break;
            questionList.add(question);
            countQuestion--;
        }

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

        if(numAiQuestions > 0) {
            aiQuestionList = getGeneratedAiQuestions(chapterId, numAiQuestions, difficulty);
        }
        aiQuestionList.forEach(aiQuestionDAO::create);
        aiQuestionList.stream()
                .map(aiQuestion -> ExamQuestion.builder()
                        .aiQuestionId(aiQuestion.getId())
                        .examId(exam.getId())
                        .questionOrder(order.getAndIncrement())
                        .build())
                .forEach(examQuestionDAO::create);

        //List question cho nguoi dung lam

        return getQuestionResponseListByQuestionListAndAiQuestionList(questionList, aiQuestionList);
    }

    private List<QuestionResponse> getQuestionResponseListByQuestionListAndAiQuestionList(List<Question> questionList, List<AiQuestion> aiQuestionList) {
        List<QuestionResponse> questionResponseList = new ArrayList<>();

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
            questionResponseList.add(questionResponse);
        }

        //Lay toan bo ai question
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

    public Exam createNewExam(Long chapterId, Long studentId) {
        // Lấy subscription đang active
        UserSubscription subscription = subscriptionService.findActiveSub(studentId);

        // Lấy plan tương ứng
        Plan plan = planService.getPlanById(subscription.getPlanId());

        // Đếm số lần đã làm bài kiểm tra
        long currentAttempts = testAttemptService
                .getAllTestAttempts().stream()
                .filter(attempt -> attempt.getUserId().equals(studentId))
                .count();

        if (currentAttempts >= plan.getMaxTestAttempts()) {
            throw new RuntimeException("Limit exam! Please buy new plan");
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
        //Cap nhat level cua user truoc khi them cau hoi
        int levelValue = updateLevelOfStudent(studentId, chapterId, examId);

        List<QuestionRequestPython> questionRequestPythonsAiQuestions = aiQuestionDAO.findAllAiQuestionDataForExam(examId);
        List<QuestionRequestPython> questionRequestPythonsQuestions = questionDAO.findAllQuestionDataForExam(examId);
        List<QuestionRequestPython> questionRequestPythons = Stream.concat(
                questionRequestPythonsAiQuestions.stream(),
                questionRequestPythonsQuestions.stream()
        ).toList();

        int firstIndexOrderQuestion = questionRequestPythons.size() + 1;

        List<Question> questionList = getNewQuestionForExam(examId, additionalQuestions, levelValue);

        AtomicInteger order = new AtomicInteger(firstIndexOrderQuestion);

        questionList.stream()
                .map(question -> ExamQuestion.builder()
                        .questionId(question.getId())
                        .examId(examId)
                        .questionOrder(order.getAndIncrement())
                        .build())
                .forEach(examQuestionDAO::create);

        List<AiQuestion> aiQuestionList = new ArrayList<>();

        int numAiQuestions = additionalQuestions - questionList.size();

        if(numAiQuestions > 0) {
            aiQuestionList = getGeneratedAiQuestions(chapterId, numAiQuestions, levelValue);
        }

        aiQuestionList.forEach(aiQuestionDAO::create);

        aiQuestionList.stream()
                .map(aiQuestion -> ExamQuestion.builder()
                        .aiQuestionId(aiQuestion.getId())
                        .examId(examId)
                        .questionOrder(order.getAndIncrement())
                        .build())
                .forEach(examQuestionDAO::create);

        return getQuestionResponseListByQuestionListAndAiQuestionList(questionList, aiQuestionList);
    }

    private List<Question> getNewQuestionForExam(Long examId, int additionalQuestions, int difficulty) {
        Exam exam = examDAO.findById(examId);
        if (exam == null) {
            return new ArrayList<>(); // hoặc ném ra một exception
        }
        Long chapterId = exam.getChapterId();

        // Bước 1: Lấy danh sách ID các câu hỏi đã dùng trong bài thi này
        List<Long> usedQuestionIds = examQuestionDAO.findUsedQuestionIdsByExamId(examId);

        // Bước 2: Lấy câu hỏi mới, loại trừ các câu đã dùng và giới hạn số lượng
        return questionDAO.findNewQuestions(chapterId, difficulty, usedQuestionIds, additionalQuestions);
    }

    public int updateLevelOfStudent(Long studentId, Long chapterId, Long examId) throws IOException {
        // Bước 1: Lấy tất cả dữ liệu câu hỏi từ DAO
        List<QuestionRequestPython> questionRequestPythonsAiQuestions = aiQuestionDAO.findAllAiQuestionDataForExam(examId);
        List<QuestionRequestPython> questionRequestPythonsQuestions = questionDAO.findAllQuestionDataForExam(examId);

        // Gộp hai danh sách lại
        List<QuestionRequestPython> allQuestions = Stream.concat(
                questionRequestPythonsAiQuestions.stream(),
                questionRequestPythonsQuestions.stream()
        ).toList();

        // Nếu bài thi không có câu hỏi nào, dừng xử lý
        if (allQuestions.isEmpty()) {
            System.out.println("Cảnh báo: Không có câu hỏi nào trong bài thi ID: " + examId);
            return 0;
        }

        // Bước 2: Xây dựng payload JSON
        JSONArray questionsData = new JSONArray();
        for (QuestionRequestPython question : allQuestions) {
            JSONObject questionData = new JSONObject();
            questionData.put("difficulty", question.getDifficulty());

            // Dùng Objects.equals() để so sánh an toàn, tự động xử lý null
            boolean isCorrect = Objects.equals(question.getStudentAnswer(), question.getCorrectAnswer());

            questionData.put("isCorrect", isCorrect);
            questionsData.put(questionData);
        }

        // Bước 3: Gọi API Python và cập nhật level
        String thetaJson = pythonApiClient.callPythonAPIForTheta(questionsData.toString());
        JSONObject thetaResponse = new JSONObject(thetaJson);
        int levelValue = thetaResponse.getInt("level");

        Level existingLevel = levelDAO.findByStudentIdAndChapterId(studentId, chapterId);
        if (existingLevel != null) {
            existingLevel.setLevel(levelValue);
            levelDAO.update(existingLevel);
        } else {
            Level newLevel = new Level();
            newLevel.setStudentId(studentId);
            newLevel.setChapterId(chapterId);
            newLevel.setLevel(levelValue);
            levelDAO.create(newLevel);
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
    
    public List<QuestionResult> getExamQuestions(long examId) {
        return examDAO.getExamQuestions(examId);
    }

    public List<Exam> getExamsByStudentId(Long studentId) {
        // Phương thức này chỉ cần gọi tới DAO để lấy dữ liệu
        return examDAO.findByStudentId(studentId);
    }


}
