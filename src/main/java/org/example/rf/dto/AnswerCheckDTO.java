package org.example.rf.dto;

import lombok.Data;

@Data
public class AnswerCheckDTO {
    private String studentAnswer;
    private String correctOption;

    public AnswerCheckDTO(String studentAnswer, String correctOption) {
        this.studentAnswer = studentAnswer;
        this.correctOption = correctOption;
    }
}
