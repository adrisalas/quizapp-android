package es.uma.quizapp.db;

import es.uma.quizapp.db.QuizContract.*;
import es.uma.quizapp.model.Question;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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
                QuestionsColumns.COLUMN_TOPIC + " TEXT, " +
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

    public void initQuestionsTable() {
        db = getWritableDatabase();
        db.execSQL("DELETE FROM " + QuestionsColumns.TABLE_NAME);

        addQuestion(new Question("Programming OOP","What is a correct syntax to output \"Hello World\" in Java?","System.out.println(\"Hello World\");", "print (\"Hello World\");", "Console.WriteLine(\"Hello World\");", " echo(\"Hello World\");", 1));
        addQuestion(new Question("Programming OOP","Which one is not a sorting algorithm?", "Bubble", "QuickSort", "Insertion", "OrderBuilder", 4));

        addQuestion(new Question("Redes Inalambricas","¿En que puerto de un servidor se reciben las peticiones http?","443", "22", "80", "12345", 3));
        addQuestion(new Question("Ciberseguridad", "¿Cuál es un algoritmo de cifrado?", "SHA-258", "SHA-3", "RST", "Assert", 2));

        addQuestion(new Question("Z_Dummy","A is correct", "A", "B", "C", "D", 1));
        addQuestion(new Question("Z_Dummy","B is correct", "A", "B", "C", "D", 2));
        addQuestion(new Question("Z_Dummy","C is correct", "A", "B", "C", "D", 3));
        addQuestion(new Question("Z_Dummy","D is correct", "A", "B", "C", "D", 4));
        addQuestion(new Question("Z_Dummy","B is correct again", "A", "B", "C", "D", 2));
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsColumns.COLUMN_TOPIC, question.getTopic());
        cv.put(QuestionsColumns.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsColumns.COLUMN_ANSWER_1, question.getOption1());
        cv.put(QuestionsColumns.COLUMN_ANSWER_2, question.getOption2());
        cv.put(QuestionsColumns.COLUMN_ANSWER_3, question.getOption3());
        cv.put(QuestionsColumns.COLUMN_ANSWER_4, question.getOption4());
        cv.put(QuestionsColumns.COLUMN_CORRECT_ANSWER, question.getAnswerNumber());
        db.insert(QuestionsColumns.TABLE_NAME, null, cv);
    }

    public ArrayList<Question> getQuestionsFromTopic(String topic) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsColumns.TABLE_NAME + " WHERE " + QuestionsColumns.COLUMN_TOPIC + " = \"" + topic + "\"", null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setTopic(c.getString(c.getColumnIndex(QuestionsColumns.COLUMN_TOPIC)));
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

    public ArrayList<String> getAllTopics() {
        ArrayList<String> topicList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT " + QuestionsColumns.COLUMN_TOPIC + " FROM " + QuestionsColumns.TABLE_NAME + " ORDER BY " + QuestionsColumns.COLUMN_TOPIC, null);

        if (c.moveToFirst()) {
            do {
                topicList.add(c.getString(c.getColumnIndex(QuestionsColumns.COLUMN_TOPIC)));
            } while (c.moveToNext());
        }
        c.close();
        return topicList;
    }

}
