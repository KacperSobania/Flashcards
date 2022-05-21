package com.flashcards.scenes.controllers.main.edit;

import com.flashcards.scenes.controllers.main.database.DatabaseConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditSet {

    @FXML
    VBox vBox;
    @FXML
    TextField titleTextField;

    final List<TextField> definitionTextField = new ArrayList<>();
    final List<TextField> answerTextField = new ArrayList<>();

    final Button deleteSetButton = createDeleteSetButton();
    final Button saveChangesButton = createSaveChangesButton();
    final Button backButton = createBackButton();

    final DatabaseConnection setsManager = DatabaseConnection.getInstance();

    public void initialize(){

        titleTextField.setText(setsManager.getTitleOfSet(setsManager.indexOfChosenSet));
        listCardDefinitionsAndAnswers();

        vBox.getChildren().add(deleteSetButton);
        vBox.getChildren().add(saveChangesButton);
        vBox.getChildren().add(backButton);
    }

    private void listCardDefinitionsAndAnswers(){

        for(int i = 1; i <= setsManager.getNumberOfCards(setsManager.indexOfChosenSet); i++) {

            Label definitionLabel = new Label();
            TextField definitionTextField = new TextField();
            Label answerLabel = new Label();
            TextField answerTextField = new TextField();

            this.definitionTextField.add(definitionTextField);
            this.answerTextField.add(answerTextField);

            definitionLabel.setPrefSize(500, 60);
            answerLabel.setPrefSize(500, 60);
            definitionTextField.setPrefSize(500, 60);
            answerTextField.setPrefSize(500, 60);

            definitionLabel.setText(this.definitionTextField.size() + ". Definition");
            answerLabel.setText(this.answerTextField.size() + ". Answer");
            definitionTextField.setText(setsManager.getCardDefinition(setsManager.indexOfChosenSet, i));
            answerTextField.setText(setsManager.getCardAnswer(setsManager.indexOfChosenSet, i));

            definitionLabel.setFont(new Font(15));
            answerLabel.setFont(new Font(15));
            definitionTextField.setFont(new Font(17));
            answerTextField.setFont(new Font(17));
            VBox.setMargin(definitionLabel, new Insets(20, 0, 0, 0));
            VBox.setMargin(answerLabel, new Insets(10, 0, 0, 0));

            vBox.getChildren().add(definitionLabel);
            vBox.getChildren().add(definitionTextField);
            vBox.getChildren().add(answerLabel);
            vBox.getChildren().add(answerTextField);
        }
    }

    private Button createDeleteSetButton(){

        Button deleteSetButton = new Button();

        deleteSetButton.setFont(new Font(20));
        deleteSetButton.setText("Delete set");
        deleteSetButton.setAlignment(Pos.CENTER);
        deleteSetButton.setMinSize(200, 60);
        VBox.setMargin(deleteSetButton, new Insets(30,0,20,0));

        deleteSetButton.setOnAction(actionEvent -> {

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

            setsManager.deleteSet(setsManager.indexOfChosenSet);
        });

        return deleteSetButton;
    }

    private Button createSaveChangesButton(){

        Button saveChangesButton = new Button();

        saveChangesButton.setFont(new Font(20));
        saveChangesButton.setText("Save changes");
        saveChangesButton.setAlignment(Pos.CENTER);
        saveChangesButton.setMinSize(200, 60);
        VBox.setMargin(saveChangesButton, new Insets(30,0,20,0));

        saveChangesButton.setOnAction(actionEvent -> {

            boolean noEmptyFields = true;
            if(titleTextField.getText().isBlank()){
                noEmptyFields = false;
                titleTextField.setText("");
                titleTextField.setPromptText("This field can't be empty!");
            }
            for(int i = 0; i < definitionTextField.size(); i++){
                if(definitionTextField.get(i).getText().isBlank()){
                    noEmptyFields = false;
                    definitionTextField.get(i).setText("");
                    definitionTextField.get(i).setPromptText("Fill this field!");
                }
                if(answerTextField.get(i).getText().isBlank()){
                    noEmptyFields = false;
                    answerTextField.get(i).setText("");
                    answerTextField.get(i).setPromptText("Fill this field!");
                }
            }

            if (noEmptyFields) {
                if(!titleTextField.getText().equals(setsManager.getTitleOfSet(setsManager.indexOfChosenSet))){
                    setsManager.updateTitleOfSet(setsManager.indexOfChosenSet, titleTextField.getText());
                }
                for(int i = 0; i < setsManager.getNumberOfCards(setsManager.indexOfChosenSet); i++){
                    if(!definitionTextField.get(i).getText().equals(setsManager.getCardDefinition(setsManager.indexOfChosenSet, i + 1))){
                        setsManager.updateDefinition(setsManager.indexOfChosenSet, i + 1, definitionTextField.get(i).getText());
                    }
                    if(!answerTextField.get(i).getText().equals(setsManager.getCardAnswer(setsManager.indexOfChosenSet, i + 1))){
                        setsManager.updateAnswer(setsManager.indexOfChosenSet, i + 1, answerTextField.get(i).getText());
                    }
                }

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
            }
        });

        return saveChangesButton;
    }

    private Button createBackButton(){

        Button backButton = new Button();

        backButton.setFont(new Font(20));
        backButton.setText("Back");
        backButton.setAlignment(Pos.CENTER);
        backButton.setMinSize(150, 60);
        VBox.setMargin(backButton, new Insets(30,0,20,0));

        backButton.setOnAction(actionEvent -> {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/flashcards/scenes/main/edit/ChooseSet.fxml"));
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

        return backButton;
    }
}
