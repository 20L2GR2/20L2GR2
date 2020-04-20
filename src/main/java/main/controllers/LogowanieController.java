package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import java.io.IOException;

public class LogowanieController {

    @FXML
    public TextField loginTextField;
    public PasswordField passwordField;
    public Label bladLogowaniaLabel;

    public void zaloguj(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        String login = loginTextField.getText();
        String haslo = passwordField.getText();
        if (login.equals("mechanik") && haslo.equals("mechanik")) {
            openWindow("/views/mechanikView.fxml", window);
            System.out.println("Zalogowano mechanik");
        } else if ((login.equals("obsluga") && haslo.equals("obsluga"))) {
            openWindow("/views/obslugaKlientaView.fxml", window);
            System.out.println("Zalogowano obsluga klienta");
        } else if ((login.equals("magazynier") && haslo.equals("magazynier"))) {
            openWindow("/views/magazynierView.fxml", window);
            System.out.println("Zalogowano magazynier");
        }
        else if ((login.equals("admin") && haslo.equals("admin"))) {
            openWindow("/views/adminView.fxml", window);
            System.out.println("Zalogowano Admin");
        }
        else {
            bladLogowaniaLabel.setVisible(true);
        }
    }

    public void logout(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        openWindow("/views/application.fxml", window);
    }

    private void openWindow(String name, Stage window) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(name));
        Scene scene = new Scene(parent,1600,800);

        window.setTitle("AutoService");
        window.setScene(scene);
        window.show();

        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setAutomaticallyColorPanes(true);
        jMetro.setScene(scene);
        parent.setStyle("-fx-font: title");
    }
}