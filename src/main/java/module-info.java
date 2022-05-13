module com.flashcards.flashcards {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.flashcards to javafx.fxml;
    exports com.flashcards;
}