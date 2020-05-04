package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MechanikController implements Initializable {
    LogowanieController mainController = new LogowanieController();

    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane czesciPane, zleceniaPane, mojeZleceniaPane, profilPane;
    @FXML
    private ToggleButton toggleButtonCzesci, toggleButtonZlecenia, toggleButtonTwojeZlecenia, toggleButtonProfil;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
    }

    public void logout(ActionEvent event) throws IOException {
        mainController.logout(event);
    }

    public void otworzCzesci() {
        System.out.println("otworzCzesci");
        borderPane.setCenter(czesciPane);
        toggleButtonCzesci.setSelected(true);
    }

    public void otworzZlecenia() {
        System.out.println("otworzZlecenia");
        borderPane.setCenter(zleceniaPane);
        toggleButtonZlecenia.setSelected(true);
    }

    public void otworzTwojeZlecenia() {
        System.out.println("otworzTwojeZlecenia");
        borderPane.setCenter(mojeZleceniaPane);
        toggleButtonTwojeZlecenia.setSelected(true);
    }

    public void otworzProfil() {
        System.out.println("otworzProfil");
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
    }

    public void zlecenieRezerwacja() {
        System.out.println("Zarezerwowano!");
    }

    public void zlecenieZakoncz() {
        System.out.println("Zakończono!");
    }

    public void czesciWyslij() {
        System.out.println("Wysłano!");
    }
}