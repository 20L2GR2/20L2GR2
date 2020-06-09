package main.controllers;

import hibernate.entity.Magazyn;
import hibernate.entity.Pracownicy;
import hibernate.entity.Zamowienia;
import hibernate.entity.Zlecenia;
import hibernate.util.HibernateUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

/**
 * Klasa wykorzystywana jako kontroler widoku mechanika. Zawiera logike, ktora jest wykorzystywana w poprawnym wyświetlaniu i obslugi widoku.
 */

public class MechanikController implements Initializable {
    LogowanieController mainController = new LogowanieController();
    List<Magazyn> magazyn;

    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane czesciPane, zleceniaPane, mojeZleceniaPane, profilPane, stanMagazynuPane;
    @FXML
    private ToggleButton toggleButtonCzesci, toggleButtonZlecenia, toggleButtonTwojeZlecenia, toggleButtonProfil, toggleButtonStanmagazyn;
    @FXML
    public Label imieLabel, nazwiskoLabel, loginLabel, blad, bladRealizacji, idTwojeZlecenie, opisUsterkaZlecenia, uzyteCzesci, bladZlecenie;
    @FXML
    private TableColumn idColumn, opisUsterkaColumn, nazwaCzesciColumn, opisColumn, iloscColumn, cenaColumn, nazwaZamowieniaColumn,
            komentarzColumn, stanColumn, nazwaCzesciMagazynColumn, opisUsterkaZleceniaColumn, idZleceniaColumn, nazwaCzesciC, opisC, iloscC, cenaC;
    @FXML
    public TableView tableZlecenia, tableMagazyn, tableZamowienia, tableTwojeZlecenia;
    @FXML
    public TextField nazwaCzesci, szukajNazwaCzesci;
    @FXML
    public TextArea komentarz, opisNaprawaZlecenia;

    /**
     * Metoda uruchamiana przy kazdym uruchomieniu widoku mechanika, dzialaca w tle na watkach Javy.
     *
     * @param url            Odniesienie do zmiennej, ktora odnosi sie do klasy URL odpowiedzialnej za uruchomienie sceny JavaFX.
     * @param resourceBundle Odniesienie do zmiennej, ktora odnosi sie do klasy ResourceBundle odpowiedzialnej za uruchomienie sceny JavaFX.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
        otworzProfil();

        szukajNazwaCzesci.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                tableMagazyn.getItems().clear();
                for (Magazyn m : magazyn) {
                    if (m.getNazwaCzesci().toLowerCase().contains(t1.toLowerCase())) {
                        tableMagazyn.getItems().add(m);
                    }
                }
            }
        });
    }

    /**
     * Metoda wykorzystywana do wylogowania danego uzytkownika.
     *
     * @param event Parametr okreslajacy konkretny widok.
     * @throws IOException Odniesienie do klasy odpowiedzialnej za zwrot obslugi bledu wyjatku.
     */

    public void logout(ActionEvent event) throws IOException {
        mainController.logout(event);
    }

    /**
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku czesci w widoku mechanika oraz odznaczenie ToggleButtonow.
     * Odpowiada takze za wyswietlenie zamowien.
     */

    public void otworzCzesci() {
        System.out.println("otworzCzesci");
        borderPane.setCenter(czesciPane);
        toggleButtonCzesci.setSelected(true);
        blad.setText("");
        tableZamowienia.getItems().clear();
        tableZamowienia.setEditable(true);

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

    /**
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku dostepnych zlecen w widoku mechanika oraz odznaczenie ToggleButtonow.
     */

    // wyświetlanie dostępnych zleceń
    public void otworzZlecenia() {
        System.out.println("otworzZlecenia");
        borderPane.setCenter(zleceniaPane);
        toggleButtonZlecenia.setSelected(true);
        tableZlecenia.getItems().clear();
        tableZlecenia.setEditable(true);
        bladRealizacji.setText("");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Zlecenia> zlecenia = session.createQuery("SELECT z FROM Zlecenia z", Zlecenia.class).getResultList();

            idColumn.setCellValueFactory(new PropertyValueFactory<>("idZlecenia"));
            opisUsterkaColumn.setCellValueFactory(new PropertyValueFactory<>("opisUsterki"));

            for (Zlecenia z : zlecenia) {
                if (z.getStanZlecenia() == 0)
                    tableZlecenia.getItems().add(z);
            }

            session.clear();
            session.disconnect();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku zlecen przypisanych do konkretnego mechanika oraz odznaczenie ToggleButtonow.
     * Odpowiada rowniez za wyswietlenie stanu co znajduje sie na magazynie.
     */

    public void otworzTwojeZlecenia() {
        System.out.println("otworzTwojeZlecenia");
        borderPane.setCenter(mojeZleceniaPane);
        toggleButtonTwojeZlecenia.setSelected(true);

        bladZlecenie.setText("");

        opisNaprawaZlecenia.setText("");
        uzyteCzesci.setText("");
        idTwojeZlecenie.setText("");
        opisUsterkaZlecenia.setText("");

        tableTwojeZlecenia.getItems().clear();
        tableTwojeZlecenia.setEditable(true);
        tableMagazyn.getItems().clear();
        tableMagazyn.setEditable(true);
        bladRealizacji.setText("");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            //wyswietlenie zlecen przypisanych do mechanika
            List<Zlecenia> zlecenia = session.createQuery("FROM Zlecenia z WHERE z.pracownikMechanik.idPracownika = :id").setParameter("id", LogowanieController.userID).getResultList();

            idZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("idZlecenia"));
            opisUsterkaZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("opisUsterki"));

            for (Zlecenia z : zlecenia) {
                if (z.getStanZlecenia() == 1)
                    tableTwojeZlecenia.getItems().add(z);
            }

            //wyswietlenie stanu magazynu
            nazwaCzesciColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaCzesci"));
            opisColumn.setCellValueFactory(new PropertyValueFactory<>("opisCzesci"));
            iloscColumn.setCellValueFactory(new PropertyValueFactory<>("ilosc"));
            cenaColumn.setCellValueFactory(new PropertyValueFactory<>("cena"));

            magazyn = session.createQuery("SELECT a FROM Magazyn a", Magazyn.class).getResultList();

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

    /**
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku informacji o konkretnym oraz odznaczenie ToggleButtonow.
     */

    public void otworzProfil() {
        System.out.println("otworzProfil");
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);

        tableZlecenia.getItems().clear();
        tableZlecenia.setEditable(true);

        // wyświetlanie użytkownika
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Pracownicy user = session.get(Pracownicy.class, LogowanieController.userID);

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

    /**
     * Metoda ladujaca informacje do wyswietlenia o danym zleceniu.
     */

    public void zlecenieZaladuj() {
        Zlecenia zlecenie = (Zlecenia) tableTwojeZlecenia.getSelectionModel().getSelectedItem();
        idTwojeZlecenie.setText(String.valueOf(zlecenie.getIdZlecenia()));
        opisUsterkaZlecenia.setText(String.valueOf(zlecenie.getOpisUsterki()));
        opisNaprawaZlecenia.setText("");
        uzyteCzesci.setText("");
    }

    /**
     * Metoda odpowiadajaca za przypisanie danej czesci do danego zlecenia.
     */

    public void czescPrzypisz() {
        Magazyn magazyn = (Magazyn) tableMagazyn.getSelectionModel().getSelectedItem();
        String czesci = uzyteCzesci.getText();
        uzyteCzesci.setText(czesci + magazyn.getNazwaCzesci() + "; ");
    }

    /**
     * Metoda konczaca zlecenie po wykonanej naprawie.
     * W metodzie sprawdzane jest wypelnianie pol.
     */

    public void zlecenieZakoncz() {
        bladZlecenie.setText("");

        String czyMozna = zlecenieZakonczCzyMozna(idTwojeZlecenie.getText(), opisNaprawaZlecenia.getText());

        if (czyMozna.equals("Zlecenie zostało zakończone")) {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();

                Zlecenia zlecenie = (Zlecenia) session.createQuery("FROM Zlecenia U WHERE U.idZlecenia = :id").setParameter("id", Integer.parseInt(idTwojeZlecenie.getText())).uniqueResult();

                zlecenie.setOpisNaprawy(opisNaprawaZlecenia.getText());
                zlecenie.setUzyteCzesci(uzyteCzesci.getText());
                zlecenie.setStanZlecenia(2);

                System.out.println(zlecenie);

                session.update(zlecenie);
                session.getTransaction().commit();
                System.out.println("Updated");
                tableZlecenia.refresh();
                otworzTwojeZlecenia();
                bladZlecenie.setStyle("-fx-text-fill: white;");
                bladZlecenie.setText(czyMozna);
                session.clear();
                session.disconnect();
                session.close();
                System.out.println("Zlecenie zakończone");
            } catch (Exception e) {
                //if (transaction != null) transaction.rollback();
                e.printStackTrace();
            }
        } else {
            bladZlecenie.setStyle("-fx-text-fill: red;");
            bladZlecenie.setText(czyMozna);
        }
    }

    /**
     * Metoda kopiujaca logike z zaloguj uzywana w testach jednostkowych.
     *
     * @param idZlecenia  Parametr przyjmuje idZlecenia.
     * @param opisNaprawy Parametr przyjmuje opis naprawy.
     * @return Zwracany jest wynik wykonania testu na podstawie danych.
     */

    public String zlecenieZakonczCzyMozna(String idZlecenia, String opisNaprawy) {
        if (idZlecenia == null || idZlecenia.equals(""))
            return "Nie wybrano zlecenia";
        else if (opisNaprawy == null || opisNaprawy.equals(""))
            return "Dodaj opis naprawy";
        else
            return "Zlecenie zostało zakończone";
    }

    /**
     * Metoda umozliwiajaca przypisanie zlecenia danemu mechanikowi.
     */

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

            Pracownicy pracownik = session.get(Pracownicy.class, LogowanieController.userID);
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

    /**
     * Metoda odpowiadajaca za wyslanie do magazyniera informacji o zapotrzebowaniu na konkretna czesc.
     */

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

            Pracownicy user = session.get(Pracownicy.class, LogowanieController.userID);

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