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

    final List<TextField> definitionTextFields = new ArrayList<>();
    final List<TextField> answerTextFields = new ArrayList<>();

    final Button deleteCardButton = createDeleteCardButton();
    final Button addCardButton = createAddCardButton();
    final Button deleteSetButton = createDeleteSetButton();
    final Button saveChangesButton = createSaveChangesButton();
    final Button backButton = createBackButton();

    final DatabaseConnection setsManager = DatabaseConnection.getInstance();

    public void initialize(){

        titleTextField.setText(setsManager.getTitleOfSet(setsManager.indexOfChosenSet));
        listCardDefinitionsAndAnswers();

        if(definitionTextFields.size() > 1){
            vBox.getChildren().add(deleteCardButton);
        }
        addFinalComponents();
    }

    private void listCardDefinitionsAndAnswers(){

        for(int i = 1; i <= setsManager.getNumberOfCards(setsManager.indexOfChosenSet); i++) {

            Label definitionLabel = new Label();
            TextField definitionTextField = new TextField();
            Label answerLabel = new Label();
            TextField answerTextField = new TextField();

            this.definitionTextFields.add(definitionTextField);
            this.answerTextFields.add(answerTextField);

            definitionLabel.setPrefSize(500, 60);
            answerLabel.setPrefSize(500, 60);
            definitionTextField.setPrefSize(500, 60);
            answerTextField.setPrefSize(500, 60);

            definitionLabel.setText(this.definitionTextFields.size() + ". Definition");
            answerLabel.setText(this.answerTextFields.size() + ". Answer");
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
            vBox.getChildren().remove(deleteSetButton);
            vBox.getChildren().remove(saveChangesButton);
            vBox.getChildren().remove(backButton);

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
            vBox.getChildren().remove(deleteSetButton);
            vBox.getChildren().remove(saveChangesButton);
            vBox.getChildren().remove(backButton);

            addNewCard();
            vBox.getChildren().add(deleteCardButton);
            addFinalComponents();
        });
        return addCardButton;
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
            for(int i = 0; i < definitionTextFields.size(); i++){
                if(definitionTextFields.get(i).getText().isBlank()){
                    noEmptyFields = false;
                    definitionTextFields.get(i).setText("");
                    definitionTextFields.get(i).setPromptText("Fill this field!");
                }
                if(answerTextFields.get(i).getText().isBlank()){
                    noEmptyFields = false;
                    answerTextFields.get(i).setText("");
                    answerTextFields.get(i).setPromptText("Fill this field!");
                }
            }

            if (noEmptyFields) {
                if(!titleTextField.getText().equals(setsManager.getTitleOfSet(setsManager.indexOfChosenSet))){
                    setsManager.updateTitleOfSet(setsManager.indexOfChosenSet, titleTextField.getText());
                }

                //delete cards from database if user deleted cards on screen
                for(int i = setsManager.getNumberOfCards(setsManager.indexOfChosenSet); i > definitionTextFields.size(); i--){
                    setsManager.deleteCard(setsManager.indexOfChosenSet, i);
                }

                //update cards
                for(int i = 0; i < setsManager.getNumberOfCards(setsManager.indexOfChosenSet); i++){
                    if(!definitionTextFields.get(i).getText().equals(setsManager.getCardDefinition(setsManager.indexOfChosenSet, i + 1))){
                        setsManager.updateDefinition(setsManager.indexOfChosenSet, i + 1, definitionTextFields.get(i).getText());
                    }
                    if(!answerTextFields.get(i).getText().equals(setsManager.getCardAnswer(setsManager.indexOfChosenSet, i + 1))){
                        setsManager.updateAnswer(setsManager.indexOfChosenSet, i + 1, answerTextFields.get(i).getText());
                    }
                }

                //add cards to database if user added cards on screen
                for(int i = setsManager.getNumberOfCards(setsManager.indexOfChosenSet); i < definitionTextFields.size(); i++){
                    setsManager.addCard(definitionTextFields.get(i).getText(), answerTextFields.get(i).getText(), setsManager.indexOfChosenSet);
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

    private void addFinalComponents(){
        vBox.getChildren().add(addCardButton);
        vBox.getChildren().add(deleteSetButton);
        vBox.getChildren().add(saveChangesButton);
        vBox.getChildren().add(backButton);
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
}
