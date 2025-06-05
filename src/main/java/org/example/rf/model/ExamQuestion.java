package org.example.rf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "exam_question")
@Builder
@AllArgsConstructor
public class ExamQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "ai_question_id")
    private Long aiQuestionId;

    @Column(name = "question_order")
    private int questionOrder;

    @Column(name = "student_answer", length = 1)
    private String studentAnswer;

    public ExamQuestion() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExam() {
        return examId;
    }

    public void setExam(Long examId) {
        this.examId = examId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getAiQuestionId() {
        return aiQuestionId;
    }

    public void setAiQuestionId(Long aiQuestionId) {
        this.aiQuestionId = aiQuestionId;
    }

    public int getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(int questionOrder) {
        this.questionOrder = questionOrder;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }
}
