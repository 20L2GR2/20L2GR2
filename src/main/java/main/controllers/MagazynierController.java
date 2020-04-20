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

public class MagazynierController implements Initializable {
    LogowanieController mainController = new LogowanieController();

    @FXML
    private BorderPane borderPane;
    @FXML
    public Pane profilPane,stanMagazynuPane,zamowieniaPane;
    @FXML
    public ToggleButton toggleButtonProfil,toggleButtonStanmagazyn,toggleButtonZamowienia;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
    }

    public void logout(ActionEvent event) throws IOException {
        mainController.logout(event);
    }

    public void otworzZamowienia() {
        System.out.println("otworzZamowienia");
        borderPane.setCenter(zamowieniaPane);
        toggleButtonZamowienia.setSelected(true);
    }

    public void otworzStanMagazyn() {
        System.out.println("otworzStanMagazynu");
        borderPane.setCenter(stanMagazynuPane);
        toggleButtonStanmagazyn.setSelected(true);
    }

    public void otworzProfil() {
        System.out.println("otworzProfil");
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
    }

    public void dodajCzesc() {
        System.out.println("Dodano!");
    }

    public void usunCzesc() {
        System.out.println("Usunięto!");
    }

    public void usunZlecenie() {
        System.out.println("Usunięto!");
    }
}