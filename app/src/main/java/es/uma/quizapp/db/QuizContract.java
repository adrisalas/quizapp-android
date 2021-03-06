package es.uma.quizapp.db;

import android.provider.BaseColumns;

import lombok.NoArgsConstructor;

@NoArgsConstructor
/**
 * Por limpieza de codigo se usa Lombok
 * https://projectlombok.org/
 */
public final class QuizContract {
    /**
     * Contrato de la tabla QUIZ_QUESTIONS
     */
    public static class QuestionsColumns implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_TOPIC = "TOPIC";
        public static final String COLUMN_QUESTION = "QUESTION";
        public static final String COLUMN_ANSWER_1 = "ANSWER_1";
        public static final String COLUMN_ANSWER_2 = "ANSWER_2";
        public static final String COLUMN_ANSWER_3 = "ANSWER_3";
        public static final String COLUMN_ANSWER_4 = "ANSWER_4";
        public static final String COLUMN_CORRECT_ANSWER = "CORRECT_ANSWER";
    }
}
