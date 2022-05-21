package com.flashcards.scenes.controllers.main.learn;

import com.flashcards.scenes.controllers.main.database.DatabaseConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    final DatabaseConnection setsManager = DatabaseConnection.getInstance();

    private final List<Integer> indexesOfCards = drawOrderOfCards();
    private int indexOfCurrentCard = 1;

    public void initialize(){

        titleOfSetLabel.setText(setsManager.getTitleOfSet(setsManager.indexOfChosenSet));
        cardLabel.setText(setsManager.getCardDefinition(setsManager.indexOfChosenSet, indexesOfCards.get(0)));

        if(setsManager.getNumberOfCards(setsManager.indexOfChosenSet) > 1){
            nextButton.setVisible(true);
        }
    }

    public void clickCard(){ //change to answer when definition is visible or to definition when answer is visible

        if(cardLabel.getText().equals(setsManager.getCardDefinition(setsManager.indexOfChosenSet, indexesOfCards.get(indexOfCurrentCard - 1)))){
            cardLabel.setText(setsManager.getCardAnswer(setsManager.indexOfChosenSet, indexesOfCards.get(indexOfCurrentCard - 1)));
        }
        else {
            cardLabel.setText(setsManager.getCardDefinition(setsManager.indexOfChosenSet, indexesOfCards.get(indexOfCurrentCard - 1)));
        }
    }

    public void clickNext(){ //show next card
        previousButton.setVisible(true);
        indexOfCurrentCard++;
        cardLabel.setText(setsManager.getCardDefinition(setsManager.indexOfChosenSet, indexesOfCards.get(indexOfCurrentCard-1)));
        if(indexOfCurrentCard >= indexesOfCards.size())
            nextButton.setVisible(false);
    }

    public void clickPrevious(){ //show previous card
        nextButton.setVisible(true);
        indexOfCurrentCard--;
        cardLabel.setText(setsManager.getCardDefinition(setsManager.indexOfChosenSet, indexesOfCards.get(indexOfCurrentCard-1)));
        if(indexOfCurrentCard <= 1){
            previousButton.setVisible(false);
        }
    }

    public void clickBack(ActionEvent event) { //back to previous scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/flashcards/scenes/main/learn/ChooseSet.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            Platform.exit();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private List<Integer> drawOrderOfCards() {
        List<Integer> drawnIndexesOfCards = new ArrayList<>();
        List<Integer> arrayWithValues = new ArrayList<>();
        for(int i = 1; i <= setsManager.getNumberOfCards(setsManager.indexOfChosenSet); i++){
            arrayWithValues.add(i);
        }
        for(int i = 0; i < arrayWithValues.size();){
            int randomNumber = new Random().nextInt(arrayWithValues.size());
            drawnIndexesOfCards.add(arrayWithValues.get(randomNumber));
            arrayWithValues.remove(randomNumber);
        }
        return drawnIndexesOfCards;
    }
}
