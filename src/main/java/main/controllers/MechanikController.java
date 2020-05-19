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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
    private Pane czesciPane, zleceniaPane, mojeZleceniaPane, profilPane, stanMagazynuPane;
    @FXML
    private ToggleButton toggleButtonCzesci, toggleButtonZlecenia, toggleButtonTwojeZlecenia, toggleButtonProfil, toggleButtonStanmagazyn;
    @FXML
    public Label imieLabel, nazwiskoLabel, loginLabel, blad, bladRealizacji, idTwojeZlecenie, opisUsterkaZlecenia, uzyteCzesci, bladZlecenie;
    @FXML
    private TableColumn idColumn, opisUsterkaColumn, nazwaCzesciColumn, opisColumn, iloscColumn, cenaColumn, nazwaZamowieniaColumn, komentarzColumn, stanColumn, nazwaCzesciMagazynColumn, opisUsterkaZleceniaColumn, idZleceniaColumn;
    @FXML
    public TableView tableZlecenia, tableMagazyn, tableZamowienia, tableTwojeZlecenia, tableZlecenieMagazyn;
    @FXML
    public TextField nazwaCzesci;
    @FXML
    public TextArea komentarz, opisNaprawaZlecenia;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
        otworzProfil();
    }

    public void logout(ActionEvent event) throws IOException {
        mainController.logout(event);
    }

    public void otworzStanMagazyn() {
        System.out.println("otworzStanMagazynu");
        borderPane.setCenter(stanMagazynuPane);
        toggleButtonStanmagazyn.setSelected(true);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            nazwaCzesciColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaCzesci"));
            opisColumn.setCellValueFactory(new PropertyValueFactory<>("opisCzesci"));
            iloscColumn.setCellValueFactory(new PropertyValueFactory<>("ilosc"));
            cenaColumn.setCellValueFactory(new PropertyValueFactory<>("cena"));

            List<Magazyn> magazyn = session.createQuery("SELECT a FROM Magazyn a", Magazyn.class).getResultList();

            for (Magazyn m : magazyn) {
                tableMagazyn.getItems().add(m);
            }

            session.clear();
            session.disconnect();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void otworzCzesci() {
        System.out.println("otworzCzesci");
        borderPane.setCenter(czesciPane);
        toggleButtonCzesci.setSelected(true);
        blad.setText("");

        // wyświetlanie zamówień
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Zamowienia> zamowienia = session.createQuery("SELECT z FROM Zamowienia z ORDER BY stanZamowienia ASC", Zamowienia.class).getResultList();


            nazwaZamowieniaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaCzesci"));
            komentarzColumn.setCellValueFactory(new PropertyValueFactory<>("komentarz"));
            stanColumn.setCellValueFactory(new PropertyValueFactory<>("stanZamowieniaToString"));
            nazwaCzesciMagazynColumn.setCellValueFactory(new PropertyValueFactory<>("czescNazwa"));

            for (Zamowienia z : zamowienia) {
                tableZamowienia.getItems().add(z);
            }

            session.clear();
            session.disconnect();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void otworzZlecenia() {
        System.out.println("otworzZlecenia");
        borderPane.setCenter(zleceniaPane);
        toggleButtonZlecenia.setSelected(true);
        tableZlecenia.getItems().clear();
        tableZlecenia.setEditable(true);
        bladRealizacji.setText("");

        // wyświetlanie dostępnych zleceń
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Zlecenia> zlecenia = session.createQuery("SELECT z FROM Zlecenia z", Zlecenia.class).getResultList();

            idColumn.setCellValueFactory(new PropertyValueFactory<>("idZlecenia"));
            opisUsterkaColumn.setCellValueFactory(new PropertyValueFactory<>("opisUsterki"));

            for (Zlecenia z : zlecenia) {
                if(z.getStanZlecenia() == 0)
                    tableZlecenia.getItems().add(z);
            }

            session.clear();
            session.disconnect();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void otworzTwojeZlecenia() {
        System.out.println("otworzTwojeZlecenia");
        borderPane.setCenter(mojeZleceniaPane);
        toggleButtonTwojeZlecenia.setSelected(true);

        bladZlecenie.setText("");

        opisNaprawaZlecenia.setText("");
        uzyteCzesci.setText("");
        idTwojeZlecenie.setText("");
        opisUsterkaZlecenia.setText("");

        tableZlecenia.getItems().clear();
        tableZlecenia.setEditable(true);
        bladRealizacji.setText("");

        // wyświetlanie dostępnych zleceń
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            //List<Zlecenia> zlecenia = session.createQuery("SELECT z FROM Zlecenia z", Zlecenia.class).getResultList();
            List<Zlecenia> zlecenia = session.createQuery("FROM Zlecenia z WHERE z.pracownikMechanik = :id").setParameter("id", LogowanieController.userID).getResultList();

            idZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("idZlecenia"));
            opisUsterkaZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("opisUsterki"));

            for (Zlecenia z : zlecenia) {
                if(z.getStanZlecenia() == 1)
                    tableZlecenia.getItems().add(z);
            }

            nazwaCzesciColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaCzesci"));
            opisColumn.setCellValueFactory(new PropertyValueFactory<>("opisCzesci"));
            iloscColumn.setCellValueFactory(new PropertyValueFactory<>("ilosc"));
            cenaColumn.setCellValueFactory(new PropertyValueFactory<>("cena"));

            List<Magazyn> magazyn = session.createQuery("SELECT a FROM Magazyn a", Magazyn.class).getResultList();

            for (Magazyn m : magazyn) {
                tableMagazyn.getItems().add(m);
            }

            session.clear();
            session.disconnect();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void otworzProfil() {
        System.out.println("otworzProfil");
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);

        tableZlecenia.getItems().clear();
        tableZlecenia.setEditable(true);

        // wyświetlanie użytkownika
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Pracownicy user = (Pracownicy) session.get(Pracownicy.class, LogowanieController.userID);

            imieLabel.setText(user.getImie());
            nazwiskoLabel.setText(user.getNazwisko());
            loginLabel.setText(user.getLogin());

            session.clear();
            session.disconnect();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zlecenieZaladuj(){
        Zlecenia zlecenie = (Zlecenia) tableTwojeZlecenia.getSelectionModel().getSelectedItem();
        idTwojeZlecenie.setText(String.valueOf(zlecenie.getIdZlecenia()));
        opisUsterkaZlecenia.setText(String.valueOf(zlecenie.getOpisUsterki()));
        opisNaprawaZlecenia.setText("");
        uzyteCzesci.setText("");
    }

    public void czescPrzypisz(){
        Magazyn magazyn = (Magazyn) tableZlecenieMagazyn.getSelectionModel().getSelectedItem();
        String czesci = uzyteCzesci.getText();
        uzyteCzesci.setText(czesci + magazyn.getNazwaCzesci() + " ");
    }

    public void zlecenieZakoncz(){
        bladZlecenie.setText("");
        if (idTwojeZlecenie.getText() == null || idTwojeZlecenie.getText().equals("")) {
            bladZlecenie.setStyle("-fx-text-fill: red;");
            bladZlecenie.setText("Nie wybrano zlecenia");
            return;
        }else if(opisNaprawaZlecenia.getText() == null || opisNaprawaZlecenia.getText().equals("")){
            bladZlecenie.setStyle("-fx-text-fill: red;");
            bladZlecenie.setText("Dodaj opis naprawy");
            return;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Zlecenia zlecenie = (Zlecenia) session.createQuery("FROM Zlecenia U WHERE U.idZlecenia = :id").setParameter("id", idTwojeZlecenie.getText()).uniqueResult();

            zlecenie.setUzyteCzesci(uzyteCzesci.getText());
            zlecenie.setStanZlecenia(2);

            System.out.println(zlecenie);

            session.update(zlecenie);
            session.getTransaction().commit();
            System.out.println("Updated");
            tableZlecenia.refresh();
            otworzTwojeZlecenia();
            bladZlecenie.setStyle("-fx-text-fill: white;");
            bladZlecenie.setText("Zlecenie zostało zakończone");
            session.clear();
            session.disconnect();
            session.close();
            System.out.println("Zlecenie zakończone");
        } catch (Exception e) {
            //if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    //rezerwacja zlecenia
    public void zlecenieRezerwacja() {
        bladRealizacji.setText("");
        if (tableZlecenia.getSelectionModel().isEmpty()) {
            bladRealizacji.setStyle("-fx-text-fill: red;");
            bladRealizacji.setText("Nie wybrano zamówienia");
            return;
        }

        Transaction transaction = null;
        Zlecenia zlecenie = null;
        Pracownicy user = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Update...");
            zlecenie = (Zlecenia) tableZlecenia.getSelectionModel().getSelectedItem();
            System.out.println(zlecenie.getIdZlecenia());

            Pracownicy pracownik = (Pracownicy) session.get(Pracownicy.class, LogowanieController.userID);
            zlecenie.setPracownikMechanik(pracownik);
            zlecenie.setStanZlecenia(1);

            System.out.println(zlecenie);

            session.update(zlecenie);
            session.getTransaction().commit();
            System.out.println("Updated");
            tableZlecenia.refresh();
            otworzZlecenia();
            bladRealizacji.setStyle("-fx-text-fill: white;");
            bladRealizacji.setText("Zlecenie zostało przypisane do Ciebie");
            session.clear();
            session.disconnect();
            session.close();
            System.out.println("Zamówienie przypisane");
        } catch (Exception e) {
            //if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
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

            Pracownicy user = (Pracownicy) session.get(Pracownicy.class, LogowanieController.userID);

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
            nazwaCzesci.clear();
            komentarz.clear();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}