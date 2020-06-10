package main.controllers;

import hibernate.entity.Pracownicy;
import hibernate.util.HibernateUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import main.PasswordHash;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Klasa wykorzystywana jako kontroler widoku logowania. Zawiera logike, ktora jest wykorzystywana w poprawnym wyświetlaniu i obslugi widoku..
 */

public class LogowanieController implements Initializable {

    public static int userID;

    @FXML
    public TextField loginTextField;
    public PasswordField passwordField;
    public Label bladLogowaniaLabel;
    public Button zalogujButton;

    /**
     * Metoda uruchamiana przy kazdym uruchomieniu widoku logowania, dzialaca w tle na watkach Javy.
     *
     * @param url            Odniesienie do zmiennej, ktora odnosi sie do klasy URL odpowiedzialnej za uruchomienie sceny JavaFX.
     * @param resourceBundle Odniesienie do zmiennej, ktora odnosi sie do klasy ResourceBundle odpowiedzialnej za uruchomienie sceny JavaFX.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> loginTextField.requestFocus());

        keyLogin(passwordField);
        keyLogin(loginTextField);

    }

    /**
     * Metoda obslugujaca klawisz ENTER wykonujaca onClick na przycisku zaloguj.
     *
     * @param field Parametr przyjmujacy aktywny textfield z okna logowania.
     */

    public void keyLogin(TextField field) {
        field.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                zalogujButton.fire();
            }
        });
    }

    /**
     * Metoda kopiujaca logike z zaloguj uzywana w testach jednostkowych.
     *
     * @param login Parametr przyjmujacy login.
     * @param haslo Parametr przyjmujacy haslo.
     * @return Zwracany jest wynik wykonania testu na podstawie danych.
     */

    public int zalogujLogic(String login, String haslo) {
        int stanowisko = -1;

        try {
            if (validate(login, haslo)) {
                Transaction transaction = null;
                Pracownicy user = null;
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    transaction = session.beginTransaction();
                    user = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.login = :login").setParameter("login", login).uniqueResult();
                    if (user != null) {
                        stanowisko = user.getStanowisko();
                        userID = user.getIdPracownika();
                        System.out.println(stanowisko);
                        session.save(stanowisko);
                    }
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    e.printStackTrace();
                }
            } else {
                bladLogowaniaLabel.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(stanowisko);
        return stanowisko;
    }

    /**
     * Metoda odpowiadająca za pobrania danych wprowadzonych przez uzytkownika, pobierajaca stanowisko pracownika.
     * Jezeli dane do zalogowania sa poprawne, przenosi do konkretnego widoku pracownika.
     *
     * @param event Parametr okreslajacy aktywny widok.
     * @throws IOException Odniesienie do klasy odpowiedzialnej za zwrot obslugi bledu wyjatku.
     * @see #validate(String, String) Poprawnosc danych wprowadzonych przez uzytkownika jest sprawdzana w tej metodzie
     */

    public void zaloguj(ActionEvent event) throws IOException {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String login = loginTextField.getText();
        String haslo = passwordField.getText();

        int stanowisko = zalogujLogic(login, haslo);

        switch (stanowisko) {
            case 0:
                openWindow("/views/adminView.fxml", window);
                break;
            case 1:
                openWindow("/views/obslugaKlientaView.fxml", window);
                break;
            case 2:
                openWindow("/views/mechanikView.fxml", window);
                break;
            case 3:
                openWindow("/views/magazynierView.fxml", window);
                break;
            default:
                bladLogowaniaLabel.setVisible(true);
                break;
        }
    }

    /**
     * Metoda wykorzystywana do wylogowania danego uzytkownika.
     *
     * @param event Parametr okreslajacy konkretny widok.
     * @throws IOException Odniesienie do klasy odpowiedzialnej za zwrot obslugi bledu wyjatku.
     */

    public void logout(ActionEvent event) throws IOException {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        openWindow("/views/application.fxml", window);
    }

    /**
     * Metoda wykorzystywana do otworzenia konkretnego widoku pracownika.
     *
     * @param name   Nazwa konkretnego widoku pracownika do uruchomienia.
     * @param window Okno potrzebne do otworzenia danego widoku.
     * @throws IOException Odniesienie do klasy odpowiedzialnej za zwrot obslugi bledu wyjatku.
     */

    private void openWindow(String name, Stage window) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(name));
        Scene scene = new Scene(parent, 1600, 800);

        window.setTitle("AutoService");
        window.setScene(scene);
        window.show();

        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setAutomaticallyColorPanes(true);
        jMetro.setScene(scene);
        parent.setStyle("-fx-font: title");
    }

    /**
     * Metoda sluzaca do sprawdzania poprawnosci wprowadzonego loginu i hasla przez uzytkownika.
     *
     * @param userName Parametr przyjmujacy login podany przez uzytkownika.
     * @param password Parametr przyjmujacy haslo podane przez uzytkownika.
     * @return Zwraca wynik poprawnosci wpisanych danych potrzebnych do zalogowania.
     */

    public boolean validate(String userName, String password) {
        Transaction transaction = null;
        Pracownicy user = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            user = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.login = :login").setParameter("login", userName).uniqueResult();
            session.clear();
            session.disconnect();
            session.close();

            return PasswordHash.combinePasswords(password, user.getHaslo());

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }
}