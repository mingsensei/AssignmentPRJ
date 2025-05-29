package org.example.rf.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ai_question")
public class AiQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "NVARCHAR(MAX)")
    private String content;

    @Column(name = "option_a", columnDefinition = "NVARCHAR(MAX)")
    private String optionA;

    @Column(name = "option_b", columnDefinition = "NVARCHAR(MAX)")
    private String optionB;

    @Column(name = "option_c", columnDefinition = "NVARCHAR(MAX)")
    private String optionC;

    @Column(name = "option_d", columnDefinition = "NVARCHAR(MAX)")
    private String optionD;

    @Column(name = "correct_option", length = 1)
    private String correctOption;

    @Column(name = "explain", columnDefinition = "NVARCHAR(MAX)")
    private String explain;

    @Column(name = "difficulty")
    private Integer difficulty;

    public AiQuestion() {}

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }
}
