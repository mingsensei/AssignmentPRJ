package org.example.rf.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QuestionRequestPython {
    String correctAnswer;
    String studentAnswer;
    int difficulty;
}
