package main.controllers;

import hibernate.entity.Magazyn;
import hibernate.entity.Pracownicy;
import hibernate.entity.Zamowienia;
import hibernate.util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
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
    public Label imieLabel, nazwiskoLabel, loginLabel;

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

    public void zlecenieRezerwacja() {
        System.out.println("Zarezerwowano!");
    }

    public void zlecenieZakoncz() {
        System.out.println("Zakończono!");
    }

    public void czesciWyslij() {
        System.out.println("Wysłano!");
    }


    private void updateData(Object object) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Update...");
            session.update(object);
            session.getTransaction().commit();
            System.out.println("Updated");
            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void inicjalizujWidokMechanikaZBazy() {


        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();


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