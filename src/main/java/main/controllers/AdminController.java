package main.controllers;

import hibernate.entity.*;
import hibernate.util.HibernateUtil;
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
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    LogowanieController mainController = new LogowanieController();


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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicjalizujWidokAdminaZBazy();
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

    public void utwórzUzytkownika() {
        if (nowyLogin.getText().isEmpty()) {
            blad.setText("Podano zle dane");
            return;
        }
        if (noweHaslo.getText().isEmpty()) {
            blad.setText("Podano zle dane");
            return;
        }
        if (nowaRola.getSelectionModel().isEmpty()) {
            blad.setText("Podano zle dane");
            return;
        }
        if (noweImie.getText().isEmpty()) {
            blad.setText("Podano zle dane");
            return;
        }
        if (noweNazwisko.getText().isEmpty()) {
            blad.setText("Podano zle dane");
            return;
        }
        Transaction transaction = null;


        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            if (!session.createQuery("SELECT a FROM Pracownicy a WHERE a.login = :login", Pracownicy.class).setParameter("login", nowyLogin.getText()).getResultList().isEmpty()) {
                blad.setText("Istnieje użytkownik z takim Loginem");
                session.clear();
                session.disconnect();
                session.close();
                return;
            }

            System.out.println("Tworzę użytkownika!");
            Pracownicy nowyPracownik = new Pracownicy();
            nowyPracownik.setHaslo(noweHaslo.getText());
            nowyPracownik.setLogin(nowyLogin.getText());
            nowyPracownik.setStanowisko(getShortFromStanowisko(nowaRola.getValue().toString()));
            nowyPracownik.setImie(noweImie.getText());
            nowyPracownik.setNazwisko(noweNazwisko.getText());
            session.save(nowyPracownik);
            //uzytkownicy.getItems().add(nowyPracownik);

            session.getTransaction().commit();
            inicjalizujWidokAdminaZBazy();
            blad.setText("Stworzyłem użytkownika " + nowyLogin.getText());

            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

    }

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

    public void usunZamowienie() {
        if (zamowienia.getSelectionModel().isEmpty()) return;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Zamowienia zamowienie = (Zamowienia) zamowienia.getSelectionModel().getSelectedItem();
            System.out.println("PRZESZEDŁEM DALEJ!!!! " + zamowienie.getIdZamowienia() * 30);
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
            List<Magazyn> magazyn = session.createQuery("SELECT a FROM Magazyn a", Magazyn.class).getResultList();
            for (Magazyn z : magazyn) {
                czesci.getItems().add(z);
            }

            zlecenia.setEditable(true);

            idZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("idZlecenia"));

            ObservableList<String> klienciList = FXCollections.observableArrayList();
            for (Klienci klient :
                    klienci) {
               klienciList.add(klient.getImie());
            }

            klientZleceniaColumn.setCellValueFactory(new PropertyValueFactory<>("klientImie"));
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

            List<Zlecenia> zleceniaList = session.createQuery("SELECT a FROM Zlecenia a", Zlecenia.class).getResultList();
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

    Pracownicy getUserByLogin(List<Pracownicy> listaPracownikow, String login) {
        for (Pracownicy pracownik :
                listaPracownikow) {
            if (pracownik.getLogin() == login) return pracownik;
        }
        return new Pracownicy();
    }

    Klienci getKlientByImie(List<Klienci> listaKlientow, String imie){
        for (Klienci klient :
                listaKlientow){
            if (klient.getImie() == imie) return klient;
        }
        return new Klienci();
    }



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

    int getStanZleceniaByText(String zlecenie){
        switch(zlecenie){
            case "zlecenie utworzone i oczekujące do przyjęcia przez mechanika": return 0;
            case "zlecenie przyjęte przez mechanika i w trakcie realizacji": return 1;
            case "zlecenie oczekujące do wyceny (mechanik wykonał naprawę)": return 2;
            case "zlecenie zakończone": return 3;
            case "zlecenie anulowane": return 4;
        }
        return 0;
    }

    short getShortFromStanowisko(String stanowisko){
        switch (stanowisko){
            case "admin": return 0;
            case "Obsługa klienta": return 1;
            case "Mechanik": return 2;
            case "Magazynier": return 3;
        }
        return 1;
    }


}