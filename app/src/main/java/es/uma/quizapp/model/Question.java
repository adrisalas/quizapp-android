package es.uma.quizapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
/**
 * Por limpieza de codigo se usa Lombok
 * https://projectlombok.org/
 */
public class Question {
    private String topic;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int answerNumber;
}
