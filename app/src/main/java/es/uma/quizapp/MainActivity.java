package es.uma.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import es.uma.quizapp.activity.LobbyQuiz;
import es.uma.quizapp.db.QuizDbHelper;
import es.uma.quizapp.util.SingletonMap;

public class MainActivity extends AppCompatActivity {

    private final SingletonMap singletonMap = SingletonMap.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_import = findViewById(R.id.button_import);
        QuizDbHelper dbHelper = new QuizDbHelper(this);

        ListView listView_topics = findViewById(R.id.listView_topics);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.button_list_item, dbHelper.getAllTopics());
        listView_topics.setAdapter(arrayAdapter);

        button_import.setOnClickListener(view -> {
            dbHelper.initQuestionsTable();
            finish();
            startActivity(getIntent());
        });
        new AlertDialog.Builder(this)
                .setMessage(R.string.advisory)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void selectTopic(View v){
        singletonMap.put("SELECTED_QUIZ",((TextView) v).getText());
        startActivity(new Intent(this, LobbyQuiz.class));
    }
}