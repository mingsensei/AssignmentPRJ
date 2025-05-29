package org.example.rf.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_question")
public class ExamQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // exam_id
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    // question_id
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    // ai_question_id
    @ManyToOne
    @JoinColumn(name = "ai_question_id")
    private AiQuestion aiQuestion;

    @Column(name = "question_order")
    private Integer questionOrder;

    @Column(name = "student_answer", length = 1)
    private String studentAnswer;

    public ExamQuestion() {}

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public AiQuestion getAiQuestion() {
        return aiQuestion;
    }

    public void setAiQuestion(AiQuestion aiQuestion) {
        this.aiQuestion = aiQuestion;
    }

    public Integer getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(Integer questionOrder) {
        this.questionOrder = questionOrder;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }
}
