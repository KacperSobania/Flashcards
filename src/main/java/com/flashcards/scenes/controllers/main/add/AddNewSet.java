package com.flashcards.scenes.controllers.main.add;

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

public class AddNewSet {

    @FXML
    VBox vBox;
    @FXML
    TextField titleTextField;

    final List<TextField> definitionTextFields = new ArrayList<>();
    final List<TextField> answerTextFields = new ArrayList<>();

    final Button addCardButton = createAddCardButton();
    final Button deleteCardButton = createDeleteCardButton();
    final Button submitButton = createSubmitButton();
    final Button backToMenuButton = createBackToMenuButton();

    final DatabaseConnection setsManager = DatabaseConnection.getInstance();

    public void initialize(){

        addNewCard(); //adds first definition & answer Labels and TextFields to VBox
        addFinalComponents(); //adds add, submit and back Buttons to VBox
    }

    private void addNewCard(){

        //set properties for Labels and TextFields
        Label definitionLabel = new Label();
        TextField definitionTextField = new TextField();
        Label answerLabel = new Label();
        TextField answerTextField = new TextField();

        this.definitionTextFields.add(definitionTextField);
        this.answerTextFields.add(answerTextField);

        definitionLabel.setPrefSize(500,60);
        answerLabel.setPrefSize(500,60);
        definitionTextField.setPrefSize(500,60);
        answerTextField.setPrefSize(500,60);

        definitionLabel.setText(this.definitionTextFields.size() + ". Definition");
        answerLabel.setText(this.answerTextFields.size() + ". Answer");

        definitionLabel.setFont(new Font(15));
        answerLabel.setFont(new Font(15));

        definitionTextField.setFont(new Font(17));
        answerTextField.setFont(new Font(17));

        VBox.setMargin(definitionLabel, new Insets(20,0,0,0));
        VBox.setMargin(answerLabel, new Insets(10,0,0,0));

        //add card to VBox
        vBox.getChildren().add(definitionLabel);
        vBox.getChildren().add(definitionTextField);
        vBox.getChildren().add(answerLabel);
        vBox.getChildren().add(answerTextField);
    }

    private Button createAddCardButton(){

        Button addCardButton = new Button();

        //set Button properties
        addCardButton.setFont(new Font(25));
        addCardButton.setText("+");
        addCardButton.setAlignment(Pos.CENTER);
        addCardButton.setMinSize(80, 50);
        VBox.setMargin(addCardButton, new Insets(20,0,0,0));

        //set action on add new card click
        addCardButton.setOnAction(actionEvent -> {

            if(definitionTextFields.size() > 1){
                vBox.getChildren().remove(deleteCardButton);
            }
            //remove final components
            vBox.getChildren().remove(addCardButton);
            vBox.getChildren().remove(submitButton);
            vBox.getChildren().remove(backToMenuButton);

            //add card and final components
            addNewCard();
            vBox.getChildren().add(deleteCardButton);
            addFinalComponents(); //add, submit and back Button
        });
        return addCardButton;
    }

    private Button createDeleteCardButton() {

        Button deleteCardButton = new Button();

        //set Button properties
        deleteCardButton.setFont(new Font(25));
        deleteCardButton.setText("-");
        deleteCardButton.setAlignment(Pos.CENTER);
        deleteCardButton.setMinSize(80, 50);
        VBox.setMargin(deleteCardButton, new Insets(20,0,0,0));

        //set action after clicking delete card
        deleteCardButton.setOnAction(actionEvent -> {

            //remove final components in VBox
            vBox.getChildren().remove(deleteCardButton);
            vBox.getChildren().remove(addCardButton);
            vBox.getChildren().remove(submitButton);
            vBox.getChildren().remove(backToMenuButton);

            //remove last card from VBox
            vBox.getChildren().remove(answerTextFields.get(answerTextFields.size() - 1));
            answerTextFields.remove(answerTextFields.get(answerTextFields.size() - 1));
            vBox.getChildren().remove(vBox.getChildren().size() - 1);
            vBox.getChildren().remove(definitionTextFields.get(definitionTextFields.size() - 1));
            definitionTextFields.remove(definitionTextFields.get(definitionTextFields.size() - 1));
            vBox.getChildren().remove(vBox.getChildren().size() - 1);

            //check if it is possible to delete more cards
            if(definitionTextFields.size() > 1){
                vBox.getChildren().add(deleteCardButton);
            }
            addFinalComponents();
        });

        return deleteCardButton;
    }

    private Button createSubmitButton(){

        Button submitButton = new Button();

        //set Button properties
        submitButton.setFont(new Font(20));
        submitButton.setText("Submit");
        submitButton.setAlignment(Pos.CENTER);
        submitButton.setMinSize(150, 60);
        VBox.setMargin(submitButton, new Insets(30,0,0,0));

        //set action after clicking submit
        submitButton.setOnAction(actionEvent -> {

            //check if TextFields are blank
            boolean noBlankFields = true;
            if(titleTextField.getText().isBlank()){
                noBlankFields = false;
                titleTextField.setText("");
                titleTextField.setPromptText("This field can't be empty!");
            }
            for(int i = 0; i < definitionTextFields.size(); i++){
                if(definitionTextFields.get(i).getText().isBlank()){
                    noBlankFields = false;
                    definitionTextFields.get(i).setText("");
                    definitionTextFields.get(i).setPromptText("Fill this field!");
                }
                if(answerTextFields.get(i).getText().isBlank()){
                    noBlankFields = false;
                    answerTextFields.get(i).setText("");
                    answerTextFields.get(i).setPromptText("Fill this field!");
                }
            }

            //if TextFields are not blank, save set
            if (noBlankFields) {

                setsManager.addTitleOfSet(titleTextField.getText()); //save title of set in database

                //save cards in database
                for(int i = 0; i < this.definitionTextFields.size(); i++){
                    setsManager.addCard(this.definitionTextFields.get(i).getText(), this.answerTextFields.get(i).getText(), setsManager.getNumberOfSets());
                }

                //load new scene
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

        return submitButton;
    }

    private Button createBackToMenuButton(){

        Button backToMenuButton = new Button();

        //set Button properties
        backToMenuButton.setFont(new Font(20));
        backToMenuButton.setText("Back to menu");
        backToMenuButton.setAlignment(Pos.CENTER);
        backToMenuButton.setMinSize(200, 60);
        VBox.setMargin(backToMenuButton, new Insets(30,0,20,0));

        //set action after clicking back to menu
        backToMenuButton.setOnAction(actionEvent -> {
            //load scene
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

        return backToMenuButton;
    }

    private void addFinalComponents(){
        vBox.getChildren().add(addCardButton);
        vBox.getChildren().add(submitButton);
        vBox.getChildren().add(backToMenuButton);

    }

}
