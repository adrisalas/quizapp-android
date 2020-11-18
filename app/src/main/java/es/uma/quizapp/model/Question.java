package es.uma.quizapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Question {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int answerNumber;
}
