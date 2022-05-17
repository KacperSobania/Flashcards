package com.flashcards.scenes.controllers.main.add;

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

public class AddNewSet {

    @FXML
    VBox vBox;
    @FXML
    TextField titleTextField;

    List<TextField> definitionTextField = new ArrayList<>();
    List<TextField> answerTextField = new ArrayList<>();

    Button addCardButton = new Button();
    Button submitButton = new Button();
    Button backToMenuButton = new Button();

    public void initialize(){
        addNewCard();

        addCardButton.setFont(new Font(25));
        addCardButton.setText("+");
        addCardButton.setAlignment(Pos.CENTER);
        addCardButton.setMinSize(80, 60);
        VBox.setMargin(addCardButton, new Insets(30,0,0,0));
        addCardButton.setOnAction(actionEvent -> {
            vBox.getChildren().remove(addCardButton);
            vBox.getChildren().remove(submitButton);
            vBox.getChildren().remove(backToMenuButton);
            addNewCard();
            addFinalComponents(); //add, submit and back button
        });

        submitButton.setFont(new Font(20));
        submitButton.setText("Submit");
        submitButton.setAlignment(Pos.CENTER);
        submitButton.setMinSize(150, 60);
        VBox.setMargin(submitButton, new Insets(30,0,0,0));
        submitButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/flashcards/scenes/main/MainMenu.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });

        backToMenuButton.setFont(new Font(20));
        backToMenuButton.setText("Back to menu");
        backToMenuButton.setAlignment(Pos.CENTER);
        backToMenuButton.setMinSize(200, 60);
        VBox.setMargin(backToMenuButton, new Insets(30,0,20,0));
        backToMenuButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/flashcards/scenes/main/MainMenu.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });

        addFinalComponents(); //add, submit and back button
    }

    private void addNewCard(){
        Label definitionLabel = new Label();
        TextField definitionTextField = new TextField();
        Label answerLabel = new Label();
        TextField answerTextField = new TextField();
        this.definitionTextField.add(definitionTextField);
        this.answerTextField.add(answerTextField);
        definitionLabel.setPrefSize(500,60);
        answerLabel.setPrefSize(500,60);
        definitionTextField.setPrefSize(500,60);
        answerTextField.setPrefSize(500,60);
        definitionLabel.setText(this.definitionTextField.size() + ". Definition");
        answerLabel.setText(this.answerTextField.size() + ". Answer");
        definitionLabel.setFont(new Font(15));
        answerLabel.setFont(new Font(15));
        definitionTextField.setFont(new Font(17));
        answerTextField.setFont(new Font(17));
        VBox.setMargin(definitionLabel, new Insets(20,0,0,0));
        VBox.setMargin(answerLabel, new Insets(10,0,0,0));

        vBox.getChildren().add(definitionLabel);
        vBox.getChildren().add(definitionTextField);
        vBox.getChildren().add(answerLabel);
        vBox.getChildren().add(answerTextField);
    }

    private void addFinalComponents(){
        vBox.getChildren().add(addCardButton);
        vBox.getChildren().add(submitButton);
        vBox.getChildren().add(backToMenuButton);

    }

}