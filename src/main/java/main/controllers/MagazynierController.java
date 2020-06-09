package main.controllers;

import hibernate.entity.Magazyn;
import hibernate.entity.Pracownicy;
import hibernate.entity.Zamowienia;
import hibernate.util.HibernateUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

/**
 * Klasa wykorzystywana jako kontroler widoku magazyniera. Zawiera logike, ktora jest wykorzystywana w poprawnym wyświetlaniu i obslugi widoku.
 */

public class MagazynierController implements Initializable {

    LogowanieController mainController = new LogowanieController();
    boolean przeniesiono = false;
    Zamowienia zamowienie = null;
    Magazyn czesc = null;
    List<Magazyn> magazyn;

    @FXML
    private BorderPane borderPane;
    @FXML
    public Pane profilPane, stanMagazynuPane, zamowieniaPane;
    @FXML
    public ToggleButton toggleButtonProfil, toggleButtonStanmagazyn, toggleButtonZamowienia;
    @FXML
    public TableView tableMagazyn, tableZamowienia;
    @FXML
    private TableColumn<Magazyn, String> nazwaCzesciColumn, opisColumn;
    @FXML
    private TableColumn<Magazyn, Long> iloscColumn;
    @FXML
    private TableColumn<Magazyn, Float> cenaColumn;
    @FXML
    private TableColumn nazwaColumn, komentarzColumn, mechanikColumn, zamowienieColumn;
    @FXML
    public Label imieLabel, nazwiskoLabel, loginLabel;
    @FXML
    public TextField nowaNazwaCzesci, nowaOpisCzesci, nowaIloscCzesci, nowaCenaCzesci, szukajNazwaCzesci;
    @FXML
    public Label blad, usuwanieCzesciLabel, bladRealizacji;
    @FXML
    public TextArea nowaCzescTextArea;

    /**
     * Metoda uruchamiana przy kazdym uruchomieniu widoku magazyniera, dzialaca w tle na watkach Javy.
     * Metoda rowniez odpowiada za akceptowanie wybranych znakow do pola.
     *
     * @param url            Odniesienie do zmiennej, ktora odnosi sie do klasy URL odpowiedzialnej za uruchomienie sceny JavaFX.
     * @param resourceBundle Odniesienie do zmiennej, ktora odnosi sie do klasy ResourceBundle odpowiedzialnej za uruchomienie sceny.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
        inicjalizujWidokMagazynieraZBazy();

        // only numbers
        nowaIloscCzesci.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) return;
            nowaIloscCzesci.setText(newValue.replaceAll("[^\\d]", ""));
        });

        // only numbers and . ,
        nowaCenaCzesci.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[1-9]*\\d?(.\\d{1,2})*")) return;
            nowaCenaCzesci.setText(newValue.replaceAll("[^[1-9]*\\d?(.\\d{1,2})*]", ""));
        });

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
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku zamowien w widoku magazyniera oraz odznaczenie ToggleButtonow.
     */

    public void otworzZamowienia() {
        System.out.println("otworzZamowienia");
        borderPane.setCenter(zamowieniaPane);
        toggleButtonZamowienia.setSelected(true);
        bladRealizacji.setText("");
        inicjalizujWidokMagazynieraZBazy();
    }

    /**
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku stanu magazynu w widoku magazyniera oraz odznaczenie ToggleButtonow.
     */

    public void otworzStanMagazyn() {
        System.out.println("otworzStanMagazynu");
        borderPane.setCenter(stanMagazynuPane);
        toggleButtonStanmagazyn.setSelected(true);
        blad.setText("");
        inicjalizujWidokMagazynieraZBazy();
    }

    /**
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku profilu magazyniera w widoku magazyniera oraz odznaczenie ToggleButtonow.
     */

    public void otworzProfil() {
        System.out.println("otworzProfil");
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
        inicjalizujWidokMagazynieraZBazy();
    }

    /**
     * Metoda kopiujaca logike z dodajCzesc uzywana w testach jednostkowych.
     *
     * @param nazwaCzesci Parametr przyjmujacy nazwe czesci.
     * @param opisCzesci  Parametr przyjmujacy opis czesci.
     * @param iloscCzesci Parametr przyjmujacy ilosc czesci.
     * @param cenaCzesci  Parametr przyjmujacy cene czesci.
     * @return Zwracany jest wynik wykonania testu na podstawie danych.
     */


    public String dodajCzescLogic(String nazwaCzesci, String opisCzesci, String iloscCzesci, String cenaCzesci) {

        if (nazwaCzesci == null || nazwaCzesci.trim().isEmpty()) {
            return "Podano zle dane";
        }
        if (opisCzesci == null || opisCzesci.trim().isEmpty()) {
            return "Podano zle dane";
        }
        if (iloscCzesci == null || iloscCzesci.trim().isEmpty()) {
            return "Podano zle dane";
        }
        if (cenaCzesci == null || cenaCzesci.trim().isEmpty()) {
            return "Podano zle dane";
        }

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            if (!session.createQuery("SELECT m FROM Magazyn m WHERE m.nazwaCzesci = :nazwa", Magazyn.class).setParameter("nazwa", nazwaCzesci).getResultList().isEmpty()) {
                session.clear();
                session.disconnect();
                session.close();
                return "Istnieje już taka część";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }

        return "Dodano";
    }

    /**
     * Metoda odpowiadajaca za dodanie nowej czesci do magazynu.
     * W metodzie sprawdzane jest, czy istnieje juz taka czesc w bazie.
     * Sprawdzana jest takze poprawnosc wprowadzonej ceny.
     */

    public void dodajCzesc() {

        blad.setStyle("-fx-text-fill: red;");

        if (nowaNazwaCzesci.getText().isEmpty()) {
            blad.setText("Proszę wypełnić wszystkie pola");
            return;
        }
        if (nowaOpisCzesci.getText().isEmpty()) {
            blad.setText("Proszę wypełnić wszystkie pola");
            return;
        }
        if (nowaIloscCzesci.getText().isEmpty()) {
            blad.setText("Proszę wypełnić wszystkie pola");
            return;
        }
        if (nowaCenaCzesci.getText().isEmpty()) {
            blad.setText("Proszę wypełnić wszystkie pola");
            return;
        }

        Float num = null;
        boolean numeric = true;
        try {
            num = Float.parseFloat(nowaCenaCzesci.getText());
        } catch (NumberFormatException e) {
            numeric = false;
        }

        if (!numeric) {
            blad.setText("Niepoprawna cena części");
            return;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            if (!session.createQuery("SELECT m FROM Magazyn m WHERE m.nazwaCzesci = :nazwa", Magazyn.class).setParameter("nazwa", nowaNazwaCzesci.getText()).getResultList().isEmpty()) {
                blad.setText("Istnieje już taka część");
                session.clear();
                session.disconnect();
                session.close();
                return;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

        String mogeUtworzyc = dodajCzescLogic(nowaNazwaCzesci.getText(), nowaOpisCzesci.getText(), nowaCenaCzesci.getText(), nowaIloscCzesci.getText());

        if (mogeUtworzyc.equals("Dodano")) {

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();

                System.out.println("Dodawanie części...");
                Magazyn nowaCzesc = new Magazyn();
                nowaCzesc.setNazwaCzesci(nowaNazwaCzesci.getText());
                nowaCzesc.setOpisCzesci(nowaOpisCzesci.getText());
                nowaCzesc.setIlosc(Integer.parseInt(nowaIloscCzesci.getText()));
                nowaCzesc.setCena(num);
                session.save(nowaCzesc);
                tableMagazyn.getItems().add(nowaCzesc);
                session.getTransaction().commit();
                czesc = nowaCzesc;
                System.out.println("Dodano!");

                if (przeniesiono) {
                    otworzZamowienia();
                    zrealizowaneButton();
                    tableZamowienia.refresh();
                }

                blad.setStyle("-fx-text-fill: white;");
                blad.setText("Dodano część");
                usuwanieCzesciLabel.setText("");

                session.clear();
                session.disconnect();
                session.close();

            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    /**
     * Metoda odpowiadajaca za usuwanie konkretnej czesci z bazy danych.
     */

    public void usunCzesc() {

        usuwanieCzesciLabel.setStyle("-fx-text-fill: red;");

        if (tableMagazyn.getSelectionModel().isEmpty()) {
            usuwanieCzesciLabel.setText("Nie wybrano części");
            return;
        }

        Magazyn usuwanieCzesci = (Magazyn) tableMagazyn.getSelectionModel().getSelectedItem();
        Zamowienia zamowieniaCzesc = ifCzescInDb(usuwanieCzesci);
        if (zamowieniaCzesc != null) {
            updateCzescZamowienia(zamowieniaCzesc);
        }
        System.out.println("Usuwanie części...");

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            System.out.println("ID części w magazynie: " + usuwanieCzesci.getIdCzesci());
            System.out.println("ID części w zamówniach: " + zamowieniaCzesc);

            session.delete(usuwanieCzesci);
            tableMagazyn.getItems().remove(usuwanieCzesci);
            session.getTransaction().commit();
            System.out.println("Usunięto część");

            usuwanieCzesciLabel.setStyle("-fx-text-fill: white;");
            usuwanieCzesciLabel.setText("Usunięto część");
            blad.setText("");

            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Metoda zabezpieczajaca kaskadowo przy usunieciu czesci z magazynu, jesli czesc zostala juz przypisana do jakiegos zamowienia.
     *
     * @param zamow Rekord przechowujacy konkretne zamowienie.
     */


    public void updateCzescZamowienia(Zamowienia zamow) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            zamow.setCzesc(null);

            session.update(zamow);
            session.getTransaction().commit();
            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Metoda sprawdzajaca czy dana czesc juz jest w bazie danych.
     *
     * @param usuwanieCzesci Sprawdzana czesc.
     * @return Zwracany rekord z informacja o danej czesci (badz null).
     */

    public Zamowienia ifCzescInDb(Magazyn usuwanieCzesci) {
        Zamowienia czescZamow;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            czescZamow = (Zamowienia) session.createQuery("FROM Zamowienia z WHERE z.czesc = :id").setParameter("id", usuwanieCzesci).uniqueResult();

            session.clear();
            session.disconnect();
            session.close();
            return czescZamow;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metoda odpowiadajaca za zmiane stanu zlecenia - do realizacji.
     */

    public void doRealizacjiButton() {
        bladRealizacji.setStyle("-fx-text-fill: red;");
        if (tableZamowienia.getSelectionModel().isEmpty()) {
            bladRealizacji.setText("Nie wybrano zamówienia");
            return;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Update...");
            Zamowienia zamowienie = (Zamowienia) tableZamowienia.getSelectionModel().getSelectedItem();
            System.out.println(zamowienie.getIdZamowienia());
            zamowienie.setStanZamowienia((short) 1);
            session.update(zamowienie);
            session.getTransaction().commit();
            System.out.println("Updated");
            tableZamowienia.refresh();
            bladRealizacji.setStyle("-fx-text-fill: white;");
            bladRealizacji.setText("Zmieniono stan zamówienia");
            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        System.out.println("Zmieniono do realizacji");
    }

    /**
     * Metoda odpowiadajaca za zmiane stanu zlecenia - zlecenia zrealizowane
     * Po zmianie stanu, metoda przenosi do widoku dodawania czesci, kopiujac dane do TextArea dla wygody uzytkownika.
     */

    public void zrealizowaneButton() {

        System.out.println(zamowienie);

        if (zamowienie != null) {
            System.out.println("Zamówienie różne od null");
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                System.out.println("Update...");

                zamowienie.setStanZamowienia((short) 2);
                System.out.println(czesc);
                zamowienie.setCzesc(czesc);
                System.out.println(zamowienie);
                if (przeniesiono) {
                    przeniesiono = false;
                    session.update(zamowienie);
                    session.getTransaction().commit();
                    System.out.println("Updated");
                    tableZamowienia.refresh();
                    bladRealizacji.setStyle("-fx-text-fill: white;");
                    bladRealizacji.setText("Zmieniono stan zamówienia");
                }

                zamowienie = null;
                session.clear();
                session.disconnect();
                session.close();
                System.out.println("Zamówienie zrealizowane");
                inicjalizujWidokMagazynieraZBazy();
                return;
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
            }
        }

        bladRealizacji.setStyle("-fx-text-fill: red;");
        if (tableZamowienia.getSelectionModel().isEmpty()) {
            bladRealizacji.setText("Nie wybrano zamówienia");
            return;
        }

        zamowienie = (Zamowienia) tableZamowienia.getSelectionModel().getSelectedItem();

        System.out.println("Id zamówienia w tabeli: " + zamowienie.getIdZamowienia());
        zamowienie.setStanZamowienia((short) 2);

        nowaCzescTextArea.setText(zamowienie.getNazwaCzesci() + "\n" + zamowienie.getKomentarz());

        przeniesiono = true;
        System.out.println("Przeniesiono: " + przeniesiono);
        otworzStanMagazyn();

    }

    /**
     * Metoda odpowiadajaca za pobranie nowych, wyedytowanych danych wiersza z tabeli w programi i aktualizacja ich w bazie danych.
     *
     * @param object Wiersz ze zmienionymi danymi, ktore dane te sa aktualizowane w bazie danych.
     */

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

    /**
     * Pobranie danych do wszystkich podwidokow dla zalogowanego magazyniera.
     */

    public void inicjalizujWidokMagazynieraZBazy() {

        tableMagazyn.getItems().clear();
        tableZamowienia.getItems().clear();

        tableMagazyn.setEditable(true);
        tableZamowienia.setEditable(true);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            List<Zamowienia> zamowienia = session.createQuery("SELECT z FROM Zamowienia z ORDER BY stanZamowienia ASC", Zamowienia.class).getResultList();


            nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaCzesci"));
            komentarzColumn.setCellValueFactory(new PropertyValueFactory<>("komentarz"));
            mechanikColumn.setCellValueFactory(new PropertyValueFactory<>("imieNazwisko"));
            zamowienieColumn.setCellValueFactory(new PropertyValueFactory<>("stanZamowieniaToString"));

            for (Zamowienia z : zamowienia) {
                tableZamowienia.getItems().add(z);
            }

            //------------------------------------------------------------

            nazwaCzesciColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaCzesci"));
            nazwaCzesciColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            nazwaCzesciColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setNazwaCzesci(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            opisColumn.setCellValueFactory(new PropertyValueFactory<>("opisCzesci"));
            opisColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            opisColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setOpisCzesci(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            iloscColumn.setCellValueFactory(new PropertyValueFactory<>("ilosc"));
            iloscColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
            iloscColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setIlosc(e.getNewValue().intValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            cenaColumn.setCellValueFactory(new PropertyValueFactory<>("cena"));
            cenaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
            cenaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setCena(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            magazyn = session.createQuery("SELECT a FROM Magazyn a", Magazyn.class).getResultList();

            for (Magazyn m : magazyn) {
                tableMagazyn.getItems().add(m);
            }

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

    /**
     * Metoda wykorzystywana w testach jednostkowych - sprawdza czy wszystkie pola sa wypelnione.
     *
     * @param nazwa Parametr przyjmuje nazwe.
     * @param opis  Parametr przyjmuje opis.
     * @param ilosc Parametr przyjmuje ilosc.
     * @param cena  Parametr przyjmuje cene.
     * @return Zwracany jest wynik wykonania testu na podstawie danych.
     */

    public String canAddPartIfAllFieldsAreSet(String nazwa, String opis, String ilosc, String cena) {
        if (nazwa == null || nazwa.equals("")) {
            return "Nie podano wszystkich danych";
        } else if (opis == null || opis.equals("")) {
            return "Nie podano wszystkich danych";
        } else if (ilosc == null || ilosc.equals("")) {
            return "Nie podano wszystkich danych";
        } else if (cena == null || cena.equals("")) {
            return "Nie podano wszystkich danych";
        } else {
            return "Dodano czesc";
        }
    }

    /**
     * Metoda wykorzystywana w testach jednostkowych - sprawdza czy ilosc zostala podana.
     *
     * @param quantity Parametr przyjmuje ilosc.
     * @return Zwracany jest wynik wykonania testu na podstawie danych.
     */

    public boolean canAddPartBeFinalizedIfQuantityIsSetProperly(String quantity) {
        boolean state = true;
        try {
            Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            state = false;
        }
        return state;
    }

    /**
     * Metoda wykorzystywana w testach jednostkowych - sprawdza czy cena zostala podana.
     *
     * @param price Parametr przyjmuje cene.
     * @return Zwracany jest wynik wykonania testu na podstawie danych.
     */

    public boolean canAddPartBeFinalizedIfPriceIsSetProperly(String price) {
        boolean state = true;
        try {
            Float.parseFloat(price);
        } catch (NumberFormatException e) {
            state = false;
        }
        return state;
    }

}