package es.uma.quizapp.db;

import es.uma.quizapp.db.QuizContract.*;
import es.uma.quizapp.model.Question;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class QuizDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "quizzappadripoky.db";
    public static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Cuando se abre la app por primera vez se ejecuta este metodo
     * @param db base de datos, android injecta este valor con el contexto
     */
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

    /**
     * Se usa para actualizar la base de datos cuando se actualice la aplicacion
     * @param db base de datos, android injecta este valor con el contexto
     * @param oldVersion version anterior de la base de datos, android injecta este valor con el contexto
     * @param newVersion version actual de la base de datos, android injecta este valor con el contexto
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsColumns.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Hace una llamada a una REST API https://quiz-android-api.herokuapp.com/
     * Para poder ejecutar en android una llamada http hace falta encapsularla en un thread Async
     * Los datos obtenidos los parseamos con el metodo privado readJson
     */
    public void initQuestionsTable() {
        db = getWritableDatabase();
        db.execSQL("DELETE FROM " + QuestionsColumns.TABLE_NAME);

        AsyncTask.execute(() -> {
            try {
                URL apiURL = new URL("https://quiz-android-api.herokuapp.com/");
                HttpsURLConnection connection = (HttpsURLConnection) apiURL.openConnection();
                connection.setRequestProperty("Accept", "application/json");
                if (connection.getResponseCode() >=200 && connection.getResponseCode() <= 300){
                    InputStream responseBody = connection.getInputStream();
                    readJson(new InputStreamReader(responseBody, StandardCharsets.UTF_8));
                    connection.disconnect();
                } else {
                    throw new Exception("API refused to give a response");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Inserta en la base de datos la pregunta
     * @param question pregunta que guardar
     */
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

    /**
     * Al pasarle "topic" tenemos que evitar SQL Injection, para eso la query realizada en este
     * metodo se parametriza con ? y new String[]{topic}
     * @return Todos las preguntas dado un topic
     */
    public ArrayList<Question> getQuestionsFromTopic(String topic) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsColumns.TABLE_NAME + " WHERE " + QuestionsColumns.COLUMN_TOPIC + " = ? ",  new String[]{topic});
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

    /**
     * @return Todos los temas/cuestionarios que estan cargados en SQLite
     */
    public List<String> getAllTopics() {
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

    /**
     * Este metodo usa JsonReader (que viene por defecto en Android), la respuesta esperada
     * es que tengamos un array de preguntas, asi que primero declaramos el array, y luego vamos iterando
     * objeto por objeto leyendo cada key (nextName) y los valores (nextString o nextInt), con esto creamos
     * un objeto Question y cuando acabamos de leer el objeto guardamos en la base de datos esta Question
     * @param responseBodyReader Body de la response http recibida
     * @throws IOException Este metodo solo esta preparado para leer el json de https://quiz-android-api.herokuapp.com/
     */
    private void readJson(InputStreamReader responseBodyReader) throws IOException {
        JsonReader jsonReader = new JsonReader(responseBodyReader);
        jsonReader.beginArray();
        while(jsonReader.hasNext()){
            jsonReader.beginObject();
            Question question = new Question();
            while(jsonReader.hasNext()){
                switch(jsonReader.nextName()){
                    case "topic":
                        question.setTopic(jsonReader.nextString());
                        break;
                    case "question":
                        question.setQuestion(jsonReader.nextString());
                        break;
                    case "option1":
                        question.setOption1(jsonReader.nextString());
                        break;
                    case "option2":
                        question.setOption2(jsonReader.nextString());
                        break;
                    case "option3":
                        question.setOption3(jsonReader.nextString());
                        break;
                    case "option4":
                        question.setOption4(jsonReader.nextString());
                        break;
                    case "answerNumber":
                        question.setAnswerNumber(jsonReader.nextInt());
                }
            }
            addQuestion(question);
            jsonReader.endObject();
        }
        jsonReader.endArray();
        jsonReader.close();
    }
}
