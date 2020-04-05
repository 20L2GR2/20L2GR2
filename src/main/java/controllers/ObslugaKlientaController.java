package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ObslugaKlientaController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obslugaKlientaBorderPane.setCenter(utworzZleceniePane);
        toogleButtonUtworzZlecenie.setSelected(true);
    }

    @FXML
    public Button buttonLogout;
    public ToggleButton toogleButtonUtworzZlecenie;
    public ToggleButton toogleButtonUkonczoneZlecenia;
    public ToggleButton toogleButtonTwojProfil;
    public Pane utworzZleceniePane;
    public Pane ukonczoneZleceniaPane;
    public Pane twojProfilPane;
    public BorderPane obslugaKlientaBorderPane;

    public void otworzTwojProfil() {
        System.out.println("otworzTwojProfil");
        obslugaKlientaBorderPane.setCenter(twojProfilPane);
        selectedButton(toogleButtonTwojProfil);
    }

    public void otworzUkonczoneZlecenia() {
        System.out.println("otworzUkonczoneZlecenia");
        obslugaKlientaBorderPane.setCenter(ukonczoneZleceniaPane);
        selectedButton(toogleButtonUkonczoneZlecenia);
    }

    public void otworzUtworzZlecenie() {
        System.out.println("otworzUtworzZlecenia");
        obslugaKlientaBorderPane.setCenter(utworzZleceniePane);
        selectedButton(toogleButtonUtworzZlecenie);
    }

    private void selectedButton(ToggleButton button) {
        button.setSelected(true);
    }

    public void dodajZlecenieButton() {
        System.out.println("Dodano nowe zlecenie button");
    }

    public void wybierzZlecenieButton() {
        System.out.println("Wybrano zlecenie button");
    }
}
