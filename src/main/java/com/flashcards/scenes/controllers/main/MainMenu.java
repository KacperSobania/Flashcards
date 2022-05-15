package com.flashcards.scenes.controllers.main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainMenu {

    @FXML
    Button addNewSetButton;
    @FXML
    Button learnButton;
    @FXML
    Button editSetsButton;
    @FXML
    Button exitButton;
    @FXML
    Label warningLabel;

    public void clickAddNewSet(ActionEvent event){

    }

    public void clickLearn(ActionEvent event){

    }

    public void clickEditSets(ActionEvent event){

    }

    public void clickExit(ActionEvent event){
        Platform.exit();
    }
}
