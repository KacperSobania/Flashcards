package com.flashcards.scenes.controllers.main.learn;

import com.flashcards.scenes.controllers.main.database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LearnSet {

    @FXML
    Label titleOfSetLabel;
    @FXML
    Label cardLabel;
    @FXML
    Button nextButton;
    @FXML
    Button previousButton;
    @FXML
    Button backButton;

    DatabaseConnection setsManager = DatabaseConnection.getInstance();

    public void initialize(){
        titleOfSetLabel.setText(setsManager.getTitleOfSet(setsManager.indexOfChosenSet));

    }

    public void clickCard(){

    }

    public void clickNext(){

    }

    public void clickPrevious(){

    }

    public void clickBack(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/flashcards/scenes/main/learn/ChooseSet.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
