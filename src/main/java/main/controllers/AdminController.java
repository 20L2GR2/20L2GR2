package main.controllers;

import hibernate.entity.*;
import hibernate.util.HibernateUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.LongStringConverter;
import main.PasswordHash;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Klasa wykorzystywana jako kontroler widoku administratora. Zawiera logike, ktora jest wykorzystywana w poprawnym wyświetlaniu i obslugi widoku.
 */

public class AdminController implements Initializable {

    LogowanieController mainController = new LogowanieController();
    @FXML
    public TextField szukajNazwaCzesci, szukajKlienta;
    List<Magazyn> magazyn;

    @FXML
    private TextField nowyLogin, noweImie, noweNazwisko;
    @FXML
    private PasswordField noweHaslo;
    @FXML
    private ChoiceBox nowaRola;
    @FXML
    private Label blad;

    @FXML
    private Label imieLabel, nazwiskoLabel, loginLabel;
    @FXML
    private TableView uzytkownicy, zamowienia, czesci, zlecenia;
    @FXML
    private TableColumn<Pracownicy, String> imieColumn, nazwiskoColumn, loginColumn, rolaColumn;
    @FXML
    private TableColumn<Magazyn, String> nazwaCzesciColumn, opisColumn;
    @FXML
    private TableColumn<Magazyn, Long> iloscColumn;
    @FXML
    private TableColumn<Magazyn, Float> cenaColumn;
    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn<Zamowienia, String> komentarzColumn, nazwaColumn, mechanikColumn, stanColumn;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane czesciPane, zleceniaPane, profilPane, utworzUzytkownikaPane, uzytkownicyPane, zamowieniaPane;
    @FXML
    private ToggleButton toggleButtonCzesci, toggleButtonZlecenia, toggleButtonProfil, toggleButtonUtworzUrz, toggleButtonUzytkownicy, toggleButtonZamowienia;
    @FXML
    private TableColumn<Zlecenia, Integer> idZleceniaColumn;
    @FXML
    private TableColumn<Zlecenia, String> klientZleceniaColumn, stanZleceniaColumn;
    @FXML
    private TableColumn<Zlecenia, String> mechanikZleceniaColumn, obslugaSZleceniaColumn, obslugaEZleceniaColumn;
    @FXML
    private TableColumn<Zlecenia, String> opisZleceniaColumn, naprwZleceniaColumn, czesciZleceniaColumn;
    @FXML
    private TableColumn<Zlecenia, Date> rozpZleceniaColumn, zakZleceniaColumn;
    @FXML
    private TableColumn<Zlecenia, Float> cenaZleceniaColumn;
    List<Zlecenia> zleceniaList;

    /**
     * Metoda uruchamiana przy kazdym uruchomieniu widoku administratora, dzialaca w tle na watkach Javy.
     *
     * @param url            Odniesienie do zmiennej, ktora odnosi sie do klasy URL odpowiedzialnej za uruchomienie sceny JavaFX.
     * @param resourceBundle Odniesienie do zmiennej, ktora odnosi sie do klasy ResourceBundle odpowiedzialnej za uruchomienie sceny JavaFX.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicjalizujWidokAdminaZBazy();
        borderPane.setCenter(zleceniaPane);
        toggleButtonZlecenia.setSelected(true);

        szukajNazwaCzesci.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                czesci.getItems().clear();
                for (Magazyn m : magazyn) {
                    if (m.getNazwaCzesci().toLowerCase().contains(t1.toLowerCase())) {
                        czesci.getItems().add(m);
                    }
                }
            }
        });

        szukajKlienta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                zlecenia.getItems().clear();
                for (Zlecenia z : zleceniaList) {
                    if (z.getImieNazwisko().toLowerCase().contains(t1.toLowerCase())) {
                        zlecenia.getItems().add(z);
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
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku czesci oraz odznaczenie ToggleButtonow.
     */

    public void otworzCzesci() {
        System.out.println("otworzCzesci");
        borderPane.setCenter(czesciPane);
        toggleButtonCzesci.setSelected(true);
    }

    /**
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku zlecen oraz odznaczenie ToggleButtonow.
     */

    public void otworzZlecenia() {
        System.out.println("otworzZlecenia");
        borderPane.setCenter(zleceniaPane);
        toggleButtonZlecenia.setSelected(true);
        inicjalizujWidokAdminaZBazy();
    }

    /**
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku tworzenia uzytkownika oraz odznaczenie ToggleButtonow.
     */

    public void otworzUtworzUzytkownika() {
        System.out.println("otworzUtworzUzytkownika");
        borderPane.setCenter(utworzUzytkownikaPane);
        toggleButtonUtworzUrz.setSelected(true);
    }

    /**
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku informacji o administratorze oraz odznaczenie ToggleButtonow.
     */

    public void otworzProfil() {
        System.out.println("otworzProfil");
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
    }

    /**
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku o uzytkownikach oraz odznaczenie ToggleButtonow.
     */

    public void otworzUzytkownicy() {
        System.out.println("otworzUzytkownicy");
        borderPane.setCenter(uzytkownicyPane);
        toggleButtonUzytkownicy.setSelected(true);
        inicjalizujWidokAdminaZBazy();
    }

    /**
     * Metoda odpowiadajaca za otworzenie i wyswietlenie podwidoku zamowien oraz odznaczenie ToggleButtonow.
     */

    public void otworzZamowienia() {
        System.out.println("otworzZamowienia");
        borderPane.setCenter(zamowieniaPane);
        toggleButtonZamowienia.setSelected(true);
        inicjalizujWidokAdminaZBazy();
    }

    /**
     * Metoda kopiujaca logike z metody utworzUzytkownika uzywana w testach jednostkowych.
     *
     * @param login    Parametr przyjmuje login.
     * @param haslo    Parametr przyjmuje haslo.
     * @param rola     Parametr przyjmuje role.
     * @param imie     Parametr przyjmuje Imie.
     * @param nazwisko Parametr przyjmuje nazwisko.
     * @return Zwracany jest wynik wykonania testu na podstawie danych.
     */

    public String utworzUzytkownikaLogic(String login, String haslo, String rola, String imie, String nazwisko) {
        if (login == null || login.trim().isEmpty()) {
            return "Podano zle dane";
        }
        if (haslo == null || haslo.trim().isEmpty()) {
            return "Podano zle dane";
        }
        if (rola == null || rola.trim().isEmpty()) {
            return "Podano zle dane";
        }
        if (imie == null || imie.trim().isEmpty()) {
            return "Podano zle dane";
        }
        if (nazwisko == null || nazwisko.trim().isEmpty()) {
            return "Podano zle dane";
        }
        Transaction transaction = null;


        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            if (!session.createQuery("SELECT a FROM Pracownicy a WHERE a.login = :login", Pracownicy.class).setParameter("login", login).getResultList().isEmpty()) {
                session.clear();
                session.disconnect();
                session.close();
                return "Istnieje użytkownik z takim Loginem";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
        return "DODANO";
    }

    /**
     * Metoda odpowiadajaca za tworzenie uzytkownika, sprawdzenie czy dane zostaly wypelnione oraz sprawdzenie czy uzytkownik z takim loginem juz istnieje
     * W metodzie tej wywolywana jest rowniez metoda hashowania podanego hasla dla nowego uzytkownika.
     */

    public void utwórzUzytkownika() {
        if (nowaRola.getSelectionModel().isEmpty()) {
            blad.setText("Podano zle dane");
            return;
        }
        String mogeUtworzyc = utworzUzytkownikaLogic(nowyLogin.getText(), noweHaslo.getText(), nowaRola.getValue().toString(), noweImie.getText(), noweNazwisko.getText());
        if (mogeUtworzyc.equals("DODANO")) {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {

                System.out.println("Tworzę użytkownika!");
                Pracownicy nowyPracownik = new Pracownicy();
                nowyPracownik.setHaslo(PasswordHash.hashPassword(noweHaslo.getText()));
                nowyPracownik.setLogin(nowyLogin.getText());
                nowyPracownik.setStanowisko(getShortFromStanowisko(nowaRola.getValue().toString()));
                nowyPracownik.setImie(noweImie.getText());
                nowyPracownik.setNazwisko(noweNazwisko.getText());
                session.save(nowyPracownik);
                //uzytkownicy.getItems().add(nowyPracownik);
                blad.setText("Stworzyłem użytkownika " + nowyLogin.getText());

                session.getTransaction().commit();
                inicjalizujWidokAdminaZBazy();


                session.clear();
                session.disconnect();
                session.close();
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
            }

        } else {
            blad.setText(mogeUtworzyc);
        }
    }

    /**
     * Metoda odpowiadajaca za usuwanie konkretnego uzytkownika.
     */

    public void usunUzytkownika() {
        if (uzytkownicy.getSelectionModel().isEmpty()) return;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Pracownicy pracownik = (Pracownicy) uzytkownicy.getSelectionModel().getSelectedItem();
            session.delete(pracownik);

            //uzytkownicy.getItems().remove(pracownik);

            session.getTransaction().commit();
            inicjalizujWidokAdminaZBazy();

            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        System.out.println("Usunieto uzytkownika!");
    }

    /**
     * Metoda odpowiadajaca za usuwanie konkretnego zlecenia z bazy.
     */

    public void usunZlecenie() {

        if (zlecenia.getSelectionModel().isEmpty()) return;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Zlecenia zlecenie = (Zlecenia) zlecenia.getSelectionModel().getSelectedItem();
            session.delete(zlecenie);

            //zlecenia.getItems().remove(zlecenie);

            session.getTransaction().commit();
            inicjalizujWidokAdminaZBazy();

            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        System.out.println("Usunieto zlecenie!");
    }

    /**
     * Metoda odpowiadajaca za usuwanie konkretnej czesci z bazy.
     */

    public void usunCzesc() {
        if (czesci.getSelectionModel().isEmpty()) return;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Magazyn czesc = (Magazyn) czesci.getSelectionModel().getSelectedItem();
            session.delete(czesc);

            //czesci.getItems().remove(czesc);

            session.getTransaction().commit();
            inicjalizujWidokAdminaZBazy();

            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        System.out.println("Usunieto czesc!");
    }

    /**
     * Metoda odpowiadajaca za usuwanie konkretnego zamowienia z bazy.
     */

    public void usunZamowienie() {
        if (zamowienia.getSelectionModel().isEmpty()) return;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Zamowienia zamowienie = (Zamowienia) zamowienia.getSelectionModel().getSelectedItem();
            System.out.println("PRZESZEDLEM DALEJ!!!! " + zamowienie.getIdZamowienia() * 30);
            session.delete(zamowienie);

            //zamowienia.getItems().remove(zamowienie);

            session.getTransaction().commit();
            inicjalizujWidokAdminaZBazy();

            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        System.out.println("Usunięto zamówienie!");
    }

    /**
     * Metoda odpowiadajaca za pobranie nowych, wyedytowanych danych wiersza z tabeli w programi i aktualizacja ich w bazie danych.
     *
     * @param obiekt Wiersz ze zmienionymi danymi, ktore dane te sa aktualizowane w bazie danych.
     */

    private void updateData(Object obiekt) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("UPDATE!");
            session.update(obiekt);
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
     * Pobranie danych do wszystkich podwidokow dla zalogowanego administratora.
     */

    public void inicjalizujWidokAdminaZBazy() {

        zlecenia.getItems().clear();
        uzytkownicy.getItems().clear();
        zamowienia.getItems().clear();
        czesci.getItems().clear();

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Pracownicy user = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.idPracownika = :id").setParameter("id", LogowanieController.userID).uniqueResult();

            imieLabel.setText(user.getImie());
            nazwiskoLabel.setText(user.getNazwisko());
            loginLabel.setText(user.getLogin());

            List<Pracownicy> pracownicy = session.createQuery("SELECT a FROM Pracownicy a", Pracownicy.class).getResultList();
            List<Klienci> klienci = session.createQuery("SELECT a FROM Klienci a", Klienci.class).getResultList();

            uzytkownicy.setEditable(true);
            imieColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
            imieColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            imieColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setImie(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
            nazwiskoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            nazwiskoColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setNazwisko(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
            loginColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            loginColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setLogin(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
                inicjalizujWidokAdminaZBazy();
            });

            rolaColumn.setCellValueFactory(new PropertyValueFactory<>("stanowiskoToString"));
            ObservableList<String> testlist = FXCollections.observableArrayList("Admin", "Obsługa klienta", "Mechanik", "Magazynier");
            rolaColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(testlist));
            rolaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setStanowisko(getShortFromStanowisko(e.getNewValue()));
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
                inicjalizujWidokAdminaZBazy();
            });

            for (Pracownicy p : pracownicy) {
                uzytkownicy.getItems().add(p);
            }
            zamowienia.setEditable(true);
            idColumn.setCellValueFactory(new PropertyValueFactory<>("idZamowienia"));

            ObservableList<String> mechanikList = FXCollections.observableArrayList();
            for (Pracownicy pracownik :
                    pracownicy) {
                if (pracownik.getStanowisko() == 2) mechanikList.add(pracownik.getLogin());
            }


            mechanikColumn.setCellValueFactory(new PropertyValueFactory<>("pracownikLogin"));
            mechanikColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(mechanikList));
            mechanikColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setPracownik(getUserByLogin(pracownicy, e.getNewValue()));
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            komentarzColumn.setCellValueFactory(new PropertyValueFactory<>("komentarz"));
            komentarzColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            komentarzColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setKomentarz(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            stanColumn.setCellValueFactory(new PropertyValueFactory<>("stanZamowieniaToString"));
            ObservableList<String> stanylist = FXCollections.observableArrayList("Zamówienie złożone przez mechanika (0)", "Zamówienie w trakcie realizacji (1)", "Zamówienie zrealizowane (2)", "Zamówienie anulowane (3)");
            stanColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(stanylist));
            stanColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setStanZamowienia(getStanZamowieniaByText(e.getNewValue()));
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaCzesci"));
            nazwaColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            nazwaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setNazwaCzesci(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });
            List<Zamowienia> zamowieniaTab = session.createQuery("SELECT a FROM Zamowienia a", Zamowienia.class).getResultList();
            for (Zamowienia z : zamowieniaTab) {
                zamowienia.getItems().add(z);
            }

            czesci.setEditable(true);
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
            for (Magazyn z : magazyn) {
                czesci.getItems().add(z);
            }

            zlecenia.setEditable(true);

            idZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("idZlecenia"));

            ObservableList<String> klienciList = FXCollections.observableArrayList();
            for (Klienci klient :
                    klienci) {
                klienciList.add(klient.getImie() + " " + klient.getNazwisko());
            }

            klientZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("imieNazwisko"));
            klientZleceniaColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(klienciList));
            klientZleceniaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setKlientZlecenie(getKlientByImie(klienci, e.getNewValue()));
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            mechanikZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("mechanikLogin"));
            mechanikZleceniaColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(mechanikList));
            mechanikZleceniaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setPracownikMechanik(getUserByLogin(pracownicy, e.getNewValue()));
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            ObservableList<String> obslugaList = FXCollections.observableArrayList();
            for (Pracownicy pracownik :
                    pracownicy) {
                if (pracownik.getStanowisko() == 1) obslugaList.add(pracownik.getLogin());
            }

            obslugaSZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("sZleceniaLogin"));
            obslugaSZleceniaColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(obslugaList));
            obslugaSZleceniaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setPracownikObslugaStart(getUserByLogin(pracownicy, e.getNewValue()));
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            obslugaEZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("eZleceniaLogin"));
            obslugaEZleceniaColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(obslugaList));
            obslugaEZleceniaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setPracownikObslugaKoniec(getUserByLogin(pracownicy, e.getNewValue()));
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            opisZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("opisUsterki"));
            opisZleceniaColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            opisZleceniaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setOpisUsterki(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            rozpZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("dataRozpoczecia"));
            rozpZleceniaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
            rozpZleceniaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setDataRozpoczecia(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            zakZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("dataZakonczenia"));
            zakZleceniaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
            zakZleceniaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setDataZakonczenia(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            naprwZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("opisNaprawy"));
            naprwZleceniaColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            naprwZleceniaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setOpisNaprawy(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            czesciZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("uzyteCzesci"));
            czesciZleceniaColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            czesciZleceniaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setUzyteCzesci(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            cenaZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("cena"));
            cenaZleceniaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
            cenaZleceniaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setCena(e.getNewValue());
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });


            stanZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("stanZleceniaToString"));
            ObservableList<String> stanyZlecenialist = FXCollections.observableArrayList("zlecenie utworzone i oczekujące do przyjęcia przez mechanika", "zlecenie przyjęte przez mechanika i w trakcie realizacji", "zlecenie oczekujące do wyceny (mechanik wykonał naprawę)", "zlecenie zakończone", "zlecenie anulowane");
            stanZleceniaColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(stanyZlecenialist));
            stanZleceniaColumn.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setStanZlecenia(getStanZleceniaByText(e.getNewValue()));
                updateData(e.getTableView().getItems().get(e.getTablePosition().getRow()));
            });

            zleceniaList = session.createQuery("SELECT a FROM Zlecenia a", Zlecenia.class).getResultList();
            for (Zlecenia z : zleceniaList) {
                zlecenia.getItems().add(z);
            }

            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }


    }

    /**
     * Metoda pobierajaca informacje o danym pracowniku na podstawie jego loginu
     *
     * @param listaPracownikow Parametr zawierajacy informacje o wszystkich pracownikach.
     * @param login            Podany login aktualnie zalogowanego pracownika.
     * @return Zwracanie wszystkich informacji o aktualnie zalogowanym pracowniku.
     */


    Pracownicy getUserByLogin(List<Pracownicy> listaPracownikow, String login) {
        for (Pracownicy pracownik :
                listaPracownikow) {
            if (pracownik.getLogin() == login) return pracownik;
        }
        return new Pracownicy();
    }

    /**
     * Metoda zwracajaca informacje o danym kliencie na podstawie podanego imienia.
     *
     * @param listaKlientow Parametr zawierajacy informacje o wszystkich klientach.
     * @param imie          Podane imie poszczegolnego klienta.
     * @return Zwracanie wszystkich informacji o poszczegolnym kliencie.
     */

    Klienci getKlientByImie(List<Klienci> listaKlientow, String imie) {
        for (Klienci klient :
                listaKlientow) {
            if ((klient.getImie() + " " + klient.getNazwisko()).equals(imie)) return klient;
        }
        return new Klienci();
    }

    /**
     * Metoda zwracajaca stan konkretnego zamowienia i tlumaczaca ten stan z postaci zapisanej w bazie danych do postaci tekstowej, zrozumialej dla uzytkownika.
     *
     * @param zamowienie Stan zamowienia zapisany tekstowo w postaci zrozumialej dla uzytkownika.
     * @return Zwrocony zostaje stan zamowienia zapisany w bazie danych.
     */

    short getStanZamowieniaByText(String zamowienie) {
        switch (zamowienie) {
            case "Zamówienie złożone przez mechanika (0)":
                return 0;
            case "Zamówienie w trakcie realizacji (1)":
                return 1;
            case "Zamówienie zrealizowane (2)":
                return 2;
            case "Zamówienie anulowane (3)":
                return 3;
            default:
                return 0;
        }
    }

    /**
     * Metoda zwracajaca stan konkretnego zlecenia i tlumaczaca ten stan z postaci zapisanej w bazie danych do postaci tekstowej, zrozumialej dla uzytkownika.
     *
     * @param zlecenie Stan zlecenia zapisany tekstowo w postaci zrozumialej dla uzytkownika.
     * @return Zwrocony zostaje stan zlecenia zapisany w bazie danych.
     */

    int getStanZleceniaByText(String zlecenie) {
        switch (zlecenie) {
            case "zlecenie utworzone i oczekujące do przyjęcia przez mechanika":
                return 0;
            case "zlecenie przyjęte przez mechanika i w trakcie realizacji":
                return 1;
            case "zlecenie oczekujące do wyceny (mechanik wykonał naprawę)":
                return 2;
            case "zlecenie zakończone":
                return 3;
            case "zlecenie anulowane":
                return 4;
        }
        return 0;
    }

    /**
     * Metoda zwracajaca nazwe konkretnego stanowiska i tlumaczaca ta nazwe z postaci liczbowej zapisanej w bazie danych do postaci tekstowej, zrozumialej dla uzytkownika.
     *
     * @param stanowisko Stanowisko zapisane slownie w postaci zrozumialej dla uzytkownika.
     * @return Zwrocone zostaje stanowisko zapisane postaci liczby w bazie danych.
     */

    short getShortFromStanowisko(String stanowisko) {
        switch (stanowisko) {
            case "admin":
                return 0;
            case "Obsługa klienta":
                return 1;
            case "Mechanik":
                return 2;
            case "Magazynier":
                return 3;
        }
        return 1;
    }


}