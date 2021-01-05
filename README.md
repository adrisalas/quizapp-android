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
- [x] 3 actividades: `MainActivity.java` `activity/LobbyQuiz.java` `activity/QuizActivity.java`
- [X] Se evita SQL Injection _(No hay ningun texto donde escriba el usuario, se interactua con botones, ergo no se puede hacer injección de código)_
- [x] SingletonMap _(Para seleccionar el quiz y guardar highscores)_
- [x] 2 Notificaciones _(Dialog en `MainActivity.java` y Toast en `activity/QuizActivity.java`)_
- [X] Soporte multidioma _(Español e Ingles)_
- [X] ArrayAdapter+ListView _(En `MainActivity.java` para seleccionar el quiz)_
- Resto de cuestiones a criterio personal
# Screenshots

![Toast](./screenshots/toastMessage.png)
![English menu](./screenshots/mainMenu_en.png)
![Spanish menu](./screenshots/mainMenu_es.png)
![Out of time](./screenshots/outOfTime.png)
![Lobby activity](./screenshots/lobbyActivity.png)