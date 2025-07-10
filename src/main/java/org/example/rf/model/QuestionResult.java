package org.example.rf.model;


public class QuestionResult {
    private int order;
    private String content;
    private String optionA, optionB, optionC, optionD;
    private String correctOption;
    private String studentAnswer;
    private boolean AIQuestion;

    public QuestionResult() {
    }

    public QuestionResult(int order, String content, String optionA, String optionB, String optionC, String optionD, String correctOption, String studentAnswer, boolean AIQuestion) {
        this.order = order;
        this.content = content;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.studentAnswer = studentAnswer;
        this.AIQuestion = AIQuestion;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public boolean isAIQuestion() {
        return AIQuestion;
    }

    public void setAIQuestion(boolean AIQuestion) {
        this.AIQuestion = AIQuestion;
    }

    public String getStatus() {
        return (studentAnswer == null ? correctOption == null : studentAnswer.equals(correctOption)) ? "correct" : "incorrect";
    }
    
    public boolean isCorrect() {
        return (correctOption == null ? studentAnswer == null : correctOption.equals(studentAnswer));
    }

    
}
