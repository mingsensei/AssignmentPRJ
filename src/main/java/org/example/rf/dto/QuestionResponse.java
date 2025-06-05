package org.example.rf.dto;

import lombok.Builder;

@Builder
public class QuestionResponse {
    private String content;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;
    private String explain;
    private Integer difficulty;
}
