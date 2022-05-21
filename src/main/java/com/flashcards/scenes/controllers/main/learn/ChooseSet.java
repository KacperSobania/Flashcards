package com.flashcards.scenes.controllers.main.learn;

import com.flashcards.scenes.controllers.main.database.DatabaseConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class ChooseSet {

    @FXML
    VBox vBox;

    final DatabaseConnection setsManager = DatabaseConnection.getInstance();

    public void initialize(){
        //add all sets to VBox
        for(int i = 1; i <= setsManager.getNumberOfSets(); i++){
            Button titleButton = new Button();
            titleButton.setMinSize(600, 70);
            titleButton.setFont(new Font(25));
            titleButton.setText(setsManager.getTitleOfSet(i));
            int indexOfCurrentSet = i;
            titleButton.setOnAction(actionEvent -> {
                setsManager.indexOfChosenSet = indexOfCurrentSet;

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/flashcards/scenes/main/learn/LearnSet.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    Platform.exit();
                }
                Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            });
            vBox.getChildren().add(titleButton);
        }

        //Button properties
        Button backToMenuButton = new Button();
        backToMenuButton.setMinSize(300,60);
        backToMenuButton.setFont(new Font(20));
        backToMenuButton.setText("Back to menu");

        backToMenuButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/flashcards/scenes/main/MainMenu.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                Platform.exit();
            }
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
        vBox.getChildren().add(backToMenuButton);
    }
}
