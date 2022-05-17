module com.flashcards.flashcards {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.flashcards to javafx.fxml;
    exports com.flashcards;
    exports com.flashcards.scenes.controllers.main;
    opens com.flashcards.scenes.controllers.main to javafx.fxml;
    exports com.flashcards.scenes.controllers.main.add;
    opens com.flashcards.scenes.controllers.main.add to javafx.fxml;
    exports com.flashcards.scenes.controllers.main.learn;
    opens com.flashcards.scenes.controllers.main.learn to javafx.fxml;
    exports com.flashcards.scenes.controllers.main.edit;
    opens com.flashcards.scenes.controllers.main.edit to javafx.fxml;
}