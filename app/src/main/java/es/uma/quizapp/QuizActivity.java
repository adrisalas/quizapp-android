package es.uma.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import es.uma.quizapp.db.QuizDbHelper;
import es.uma.quizapp.model.Question;

public class QuizActivity extends AppCompatActivity {

    public static final String extraScore = "extraScore";
    private static final long countDown = 31000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRadioButton;
    private ColorStateList textColorDefaultCounterDown;
    private CountDownTimer countDownTimer;
    private long timeLeft;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;
    private long getBackButtonTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score_count);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        radioGroup = findViewById(R.id.radio_group);
        radioButton1 = findViewById(R.id.radio_button1);
        radioButton2 = findViewById(R.id.radio_button2);
        radioButton3 = findViewById(R.id.radio_button3);
        radioButton4 = findViewById(R.id.radio_button4);
        buttonConfirmNext = findViewById(R.id.button_confirm);

        textColorDefaultRadioButton = radioButton1.getTextColors();
        textColorDefaultCounterDown = textViewCountDown.getTextColors();

        if(savedInstanceState == null) {
            QuizDbHelper dbHelper = new QuizDbHelper(this);

            questionList = dbHelper.getAllQuestions();
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);
            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_LIST);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeft = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);
            if (!answered) {
                startCountDown();
            } else {
                updateCountDown();
                showSolution();
            }
        }
        buttonConfirmNext.setOnClickListener(view -> {
            if (!answered) {
                if (radioButton1.isChecked() || radioButton2.isChecked() || radioButton3.isChecked() || radioButton4.isChecked()) {
                    checkAnswer();
                } else {
                    Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNextQuestion();
            }
        });
    }
    private void showNextQuestion() {
        radioButton1.setTextColor(textColorDefaultRadioButton);
        radioButton2.setTextColor(textColorDefaultRadioButton);
        radioButton3.setTextColor(textColorDefaultRadioButton);
        radioButton4.setTextColor(textColorDefaultRadioButton);
        radioGroup.clearCheck();
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            radioButton1.setText(currentQuestion.getOption1());
            radioButton2.setText(currentQuestion.getOption2());
            radioButton3.setText(currentQuestion.getOption3());
            radioButton4.setText(currentQuestion.getOption4());
            questionCounter++;
            String questionCount = "Question: " + questionCounter + "/" + questionCountTotal;
            textViewQuestionCount.setText(questionCount);
            answered = false;
            String confirmNext = "Confirm";
            buttonConfirmNext.setText(confirmNext);
            timeLeft = countDown;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateCountDown();
            }
            @Override
            public void onFinish() {
                timeLeft = 0;
                updateCountDown();
                checkAnswer();
            }
        };
        countDownTimer.start();
    }

    private void updateCountDown() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        String timeFormattedCountDown = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormattedCountDown);
        if (timeLeft <= 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCounterDown);
        }
    }

    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());
        int answerNumber = radioGroup.indexOfChild(rbSelected) + 1;
        if (answerNumber == currentQuestion.getAnswerNumber()) {
            score++;
            String viewScore = "Score: ";
            textViewScore.setText(viewScore);
        }
        showSolution();
    }
    private void showSolution() {
        radioButton1.setTextColor(Color.RED);
        radioButton2.setTextColor(Color.RED);
        radioButton3.setTextColor(Color.RED);
        radioButton4.setTextColor(Color.RED);
        String viewQuestion;
        switch (currentQuestion.getAnswerNumber()) {
            case 1:
                radioButton1.setTextColor(Color.GREEN);
                viewQuestion = "Answer A is correct";
                textViewQuestion.setText(viewQuestion);
                break;
            case 2:
                radioButton2.setTextColor(Color.GREEN);
                viewQuestion = "Answer B is correct";
                textViewQuestion.setText(viewQuestion);
                break;
            case 3:
                radioButton3.setTextColor(Color.GREEN);
                viewQuestion = "Answer C is correct";
                textViewQuestion.setText(viewQuestion);
                break;
            case 4:
                radioButton4.setTextColor(Color.GREEN);
                viewQuestion = "Answer D is correct";
                textViewQuestion.setText(viewQuestion);
                break;
        }
        String confirmNext;
        if (questionCounter == questionCountTotal) {
            confirmNext = "Finish";
        } else {
            confirmNext = "Next";
        }
        buttonConfirmNext.setText(confirmNext);
    }
    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(extraScore, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getBackButtonTime + 1000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        getBackButtonTime = System.currentTimeMillis();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeft);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }

    @Override
    protected void onDestroy() {super.onDestroy();
    if(countDownTimer != null){
        countDownTimer.cancel();
    }
    }

}