package main.controllers;

import hibernate.entity.Magazyn;
import hibernate.entity.Pracownicy;
import hibernate.entity.Zamowienia;
import hibernate.entity.Zlecenia;
import hibernate.util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.LongStringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MechanikController implements Initializable {
    LogowanieController mainController = new LogowanieController();

    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane czesciPane, zleceniaPane, mojeZleceniaPane, profilPane;
    @FXML
    private ToggleButton toggleButtonCzesci, toggleButtonZlecenia, toggleButtonTwojeZlecenia, toggleButtonProfil;
    @FXML
    public Label imieLabel, nazwiskoLabel, loginLabel, blad, bladRealizacji;
    @FXML
    private TableColumn idColumn, opisUsterkaColumn;
    @FXML
    public TableView tableZlecenia;
    @FXML
    public TextField nazwaCzesci;
    @FXML
    public TextArea komentarz;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
        inicjalizujWidokMechanikaZBazy();
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

    //rezerwacja zlecenia
    public void zlecenieRezerwacja() {
        bladRealizacji.setStyle("-fx-text-fill: red;");
        if (tableZlecenia.getSelectionModel().isEmpty()) {
            bladRealizacji.setText("Nie wybrano zamówienia");
            return;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Update...");
            Zlecenia zlecenie = (Zlecenia) tableZlecenia.getSelectionModel().getSelectedItem();
            System.out.println(zlecenie.getIdZlecenia());

            Pracownicy user = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.idPracownika = :id").setParameter("id", LogowanieController.userID).uniqueResult();
            zlecenie.setPracownikMechanik(user);

            session.update(zlecenie);
            session.getTransaction().commit();
            System.out.println("Updated");
            tableZlecenia.refresh();
            bladRealizacji.setStyle("-fx-text-fill: white;");
            bladRealizacji.setText("Zlecenie zostało przypisane do Ciebie");
            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        System.out.println("Zamówienie zrealizowane");
    }

    public void zlecenieZakoncz() {
        System.out.println("Zakończono!");
    }

    //dodanie rekordu do zamówienia
    public void czesciWyslij() {
        blad.setStyle("-fx-text-fill: red;");

        if (nazwaCzesci.getText().isEmpty()) {
            blad.setText("Proszę podać nazwę części!");
            return;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Pracownicy user = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.idPracownika = :id").setParameter("id", LogowanieController.userID).uniqueResult();

            System.out.println("Dodawanie części...");
            Zamowienia nowaCzesc = new Zamowienia();
            nowaCzesc.setNazwaCzesci(nazwaCzesci.getText());
            nowaCzesc.setKomentarz(komentarz.getText());
            nowaCzesc.setPracownik(user);
            session.save(nowaCzesc);
            session.getTransaction().commit();
            System.out.println("Dodano!");

            blad.setStyle("-fx-text-fill: white;");
            blad.setText("Dodano zamówienie części");

            session.clear();
            session.disconnect();
            session.close();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // wyświetlenie początkowych danych z bazy danych
    public void inicjalizujWidokMechanikaZBazy() {

        tableZlecenia.getItems().clear();
        tableZlecenia.setEditable(true);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // wyświetlanie dostępnych zleceń
            List<Zlecenia> zlecenia = session.createQuery("SELECT z FROM Zlecenia z", Zlecenia.class).getResultList();

            idColumn.setCellValueFactory(new PropertyValueFactory<>("idZlecenia"));
            opisUsterkaColumn.setCellValueFactory(new PropertyValueFactory<>("opisUsterki"));

            for (Zlecenia z : zlecenia) {
                tableZlecenia.getItems().add(z);
            }

            // wyświetlanie użytkownika
            Pracownicy user = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.idPracownika = :id").setParameter("id", LogowanieController.userID).uniqueResult();

            imieLabel.setText(user.getImie());
            nazwiskoLabel.setText(user.getNazwisko());
            loginLabel.setText(user.getLogin());

            session.clear();
            session.disconnect();
            session.close();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}