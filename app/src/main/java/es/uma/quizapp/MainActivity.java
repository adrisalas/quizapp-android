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

/**
 * IMPORTANTE En la root del proyecto tienes un README.md para ayudarte a ejecutar la app
 * Hace falta un plugin de Android Studio llamado Lombok
 */
public class MainActivity extends AppCompatActivity {

    private final SingletonMap singletonMap = SingletonMap.getInstance();

    /**
     * Se crea un ArrayAdapter y un ListView a los cuales renderizan los topic
     * de la base de datos.
     * Al clickear el boton de import se carga en la base de datos todas las preguntas
     * y se refresca la actividad
     * Ademas se crea un AlertDialog (notificacion) para mostrar un aviso sobre
     * el soporte de idiomas
     * @param savedInstanceState android injecta este atributo
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuizDbHelper dbHelper = new QuizDbHelper(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.button_list_item, dbHelper.getAllTopics());
        ListView listView_topics = findViewById(R.id.listView_topics);
        listView_topics.setAdapter(arrayAdapter);

        Button button_import = findViewById(R.id.button_import);
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

    /**
     * Se llama al pulsar un elemento de la ListView, mete en el singletonMap el topic
     * y carga la siguiente activity, la cual hace uso del topic seleccionado
     * @param v TextView del "boton" seleccionado, las ListView usan TextView
     */
    public void selectTopic(View v){
        singletonMap.put("SELECTED_QUIZ",((TextView) v).getText());
        startActivity(new Intent(this, LobbyQuiz.class));
    }
}