package es.uma.quizapp.db;

import es.uma.quizapp.db.QuizContract.*;
import es.uma.quizapp.model.Question;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuizDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "empotrados.db";
    public static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        db.execSQL("CREATE TABLE " + QuestionsColumns.TABLE_NAME +
                " ( " + QuestionsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsColumns.COLUMN_QUESTION + " TEXT, " +
                QuestionsColumns.COLUMN_ANSWER_1 + " TEXT, " +
                QuestionsColumns.COLUMN_ANSWER_2 + " TEXT, " +
                QuestionsColumns.COLUMN_ANSWER_3 + " TEXT, " +
                QuestionsColumns.COLUMN_ANSWER_4 + " TEXT, " +
                QuestionsColumns.COLUMN_ANSWER_4 + " TEXT, " +
                QuestionsColumns.COLUMN_CORRECT_ANSWER + " INTEGER" + ")");
        initQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsColumns.TABLE_NAME);
        onCreate(db);
    }

    private void initQuestionsTable() {
        addQuestion(new Question("A is correct", "A", "B", "C", "D", 1));
        addQuestion(new Question("B is correct", "A", "B", "C", "D", 2));
        addQuestion(new Question("C is correct", "A", "B", "C", "D", 3));
        addQuestion(new Question("D is correct", "A", "B", "C", "D", 4));
        addQuestion(new Question("B is correct again", "A", "B", "C", "D", 2));
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsColumns.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsColumns.COLUMN_ANSWER_1, question.getOption1());
        cv.put(QuestionsColumns.COLUMN_ANSWER_2, question.getOption2());
        cv.put(QuestionsColumns.COLUMN_ANSWER_3, question.getOption3());
        cv.put(QuestionsColumns.COLUMN_CORRECT_ANSWER, question.getAnswerNumber());
        db.insert(QuestionsColumns.TABLE_NAME, null, cv);
    }
}
