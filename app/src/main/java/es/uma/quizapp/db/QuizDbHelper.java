package es.uma.quizapp.db;

import es.uma.quizapp.db.QuizContract.*;
import es.uma.quizapp.model.Question;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
        cv.put(QuestionsColumns.COLUMN_ANSWER_4, question.getOption4());
        cv.put(QuestionsColumns.COLUMN_CORRECT_ANSWER, question.getAnswerNumber());
        db.insert(QuestionsColumns.TABLE_NAME, null, cv);
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsColumns.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsColumns.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsColumns.COLUMN_ANSWER_1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsColumns.COLUMN_ANSWER_2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsColumns.COLUMN_ANSWER_3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsColumns.COLUMN_ANSWER_4)));
                question.setAnswerNumber(c.getInt(c.getColumnIndex(QuestionsColumns.COLUMN_CORRECT_ANSWER)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
