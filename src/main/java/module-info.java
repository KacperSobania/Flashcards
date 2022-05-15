module com.flashcards.flashcards {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.flashcards to javafx.fxml;
    exports com.flashcards;
    exports com.flashcards.scenes.controllers.main;
    opens com.flashcards.scenes.controllers.main to javafx.fxml;
}