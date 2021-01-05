package es.uma.quizapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import es.uma.quizapp.R;
import es.uma.quizapp.util.SingletonMap;

/**
 * Pantalla previa al cuestionario, muestra el highscore previo
 */
public class LobbyQuiz extends AppCompatActivity {

    private final SingletonMap singletonMap = SingletonMap.getInstance();

    /**
     * Al crear la actividad llamamos al metodo load
     * y hacemos un boton para pasar a la siguiente actividad
     * @param savedInstanceState android injecta este atributo
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_quiz);

        load();

        Button button_startQuiz = findViewById(R.id.button_start_quiz);
        button_startQuiz.setOnClickListener(view -> startActivityForResult(new Intent(this, QuizActivity.class),1));
    }

    /**
     * Si hemos perdido el foco de la actividad (es decir, hemos entrado a otra)
     * y volvemos a esta actividad, recargaremos la actividad para actualizar el highscore
     */
    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    /**
     * Cargamos los datos del singletonMap en la pantalla
     */
    private void load() {
        TextView text_title = findViewById(R.id.text_title);
        TextView text_highScore = findViewById(R.id.text_highscore);

        String selectedQuiz = (String) singletonMap.get("SELECTED_QUIZ");
        text_title.setText(selectedQuiz);
        String highScore = getString(R.string.highscore) + " " + (Integer) singletonMap.getOrDefault(selectedQuiz, 0);
        text_highScore.setText(highScore);
    }
}