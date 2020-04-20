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

public class AdminController implements Initializable {
    LogowanieController mainController = new LogowanieController();

    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane czesciPane,zleceniaPane,profilPane, utworzUzytkownikaPane, uzytkownicyPane, zamowieniaPane;
    @FXML
    private ToggleButton toggleButtonCzesci, toggleButtonZlecenia, toggleButtonProfil, toggleButtonUtworzUrz, toggleButtonUzytkownicy, toggleButtonZamowienia;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.setCenter(zleceniaPane);
        toggleButtonZlecenia.setSelected(true);
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

    public void otworzUtworzUzytkownika() {
        System.out.println("otworzUtworzUzytkownika");
        borderPane.setCenter(utworzUzytkownikaPane);
        toggleButtonUtworzUrz.setSelected(true);
    }

    public void otworzProfil() {
        System.out.println("otworzProfil");
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
    }

    public void otworzUzytkownicy() {
        System.out.println("otworzUzytkownicy");
        borderPane.setCenter(uzytkownicyPane);
        toggleButtonUzytkownicy.setSelected(true);
    }

    public void otworzZamowienia() {
        System.out.println("otworzZamowienia");
        borderPane.setCenter(zamowieniaPane);
        toggleButtonZamowienia.setSelected(true);
    }

    public void utwórzUzytkownika() { System.out.println("Stworzono użytkownika!"); }

    public void usunUzytkownika() {
        System.out.println("Usunięto Użytkownika!");
    }

    public void usunZlecenie() {
        System.out.println("Usunieto zlecenie!");
    }

    public void usunCzesc() {
        System.out.println("Usunieto czesc!");
    }

    public void usunZamowienie() {
        System.out.println("Usunięto zamówienie!");
    }
}