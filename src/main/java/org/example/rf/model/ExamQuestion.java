package org.example.rf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exam_question")
@Builder
@AllArgsConstructor
@Getter
@Setter
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

    @Setter
    @Column(name = "student_answer", length = 1)
    private String studentAnswer;

    public ExamQuestion() {

    }
}