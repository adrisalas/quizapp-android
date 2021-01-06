# How to run it

1. Open the project with Android Studio _(4.1.1)_
2. Activate Lombok Plugin
   1. Go to `File -> Settings -> Plugins` and search for Lombok
   2. Install Lombok
   3. Enable Lombok Annotations
      1. Go to `File -> Settings -> Languages & Frameworks -> Lombok plugin`
      2. Check "Enable Lombok plugin for this project"
3. Restart Android Studio

# Change language

The app supports spanish and english, change the language of your mobile phone to switch between them: `Settings -> System -> Languages & Input`

# Rúbrica

Importante, la app necesita de acceso a internet para importar los cuestionarios (consulta una REST API), en caso de no tener acceso a internet no se descargara ningun quiz y no se podra usar la app. _(El emulador por defecto se conecta a internet)_

- [x] 3 actividades: `MainActivity.java` `activity/LobbyQuiz.java` `activity/QuizActivity.java`
- [x] SingletonMap _(Para seleccionar el quiz y guardar highscores)_
- [x] 2 Notificaciones _(AlertDialog en `MainActivity.java` y Toast en `activity/QuizActivity.java`)_
- [x] Se evita SQL Injection _(No hay ningun texto donde escriba el usuario, se interactua con botones. Ademas todas las funciones que reciben un valor externo, este valor se parametriza con ? y new String[]{...})_
- [x] Soporte multidioma _(Español e Ingles)_ sin cadenas a "fuego"
- [x] ArrayAdapter + ListView _(En `MainActivity.java` para seleccionar el quiz)_
- [x] Todos los métodos estan documentados
- [x] Originalidad: Con las clases online hemos hecho una app de test que se pueden cargar desde un servidor que funciona con una REST API, incentivando la repetición de los mismos como con las baterias de test del examen de conduccion
- Resto de cuestiones a criterio personal

# Screenshots

![Toast](./screenshots/toastMessage.png)
![English menu](./screenshots/mainMenu_en.png)
![Spanish menu](./screenshots/mainMenu_es.png)
![Out of time](./screenshots/outOfTime.png)
![Lobby activity](./screenshots/lobbyActivity.png)
