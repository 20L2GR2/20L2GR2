package main.controllers;

import hibernate.entity.Magazyn;
import hibernate.entity.Pracownicy;
import hibernate.entity.Zamowienia;
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

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;
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
    private TableView uzytkownicy, zamowienia, czesci;
    @FXML
    private TableColumn imieColumn, nazwiskoColumn, loginColumn, rolaColumn, nazwaColumn, komentarzColumn, mechanikColumn, stanColumn, nazwaCzesciColumn, iloscColumn, opisColumn, cenaColumn, idColumn;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane czesciPane,zleceniaPane,profilPane, utworzUzytkownikaPane, uzytkownicyPane, zamowieniaPane;
    @FXML
    private ToggleButton toggleButtonCzesci, toggleButtonZlecenia, toggleButtonProfil, toggleButtonUtworzUrz, toggleButtonUzytkownicy, toggleButtonZamowienia;

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
        if(nowyLogin.getText().isEmpty())
            {blad.setText("Podano zle dane"); return;}
        if(noweHaslo.getText().isEmpty())
            {blad.setText("Podano zle dane"); return;}
        if(nowaRola.getSelectionModel().isEmpty())
            {blad.setText("Podano zle dane"); return;}
        if(noweImie.getText().isEmpty())
            {blad.setText("Podano zle dane"); return;}
        if(noweNazwisko.getText().isEmpty())
            {blad.setText("Podano zle dane"); return;}
        Transaction transaction = null;


        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            if(!session.createQuery("SELECT a FROM Pracownicy a WHERE a.login = :login", Pracownicy.class).setParameter("login",nowyLogin.getText()).getResultList().isEmpty()) {
                blad.setText("Istnieje użytkownik z takim Loginem"); session.clear();
                session.disconnect();
                session.close();
                return;
            }

            System.out.println("Tworzę użytkownika!");
            Pracownicy nowyPracownik = new Pracownicy();
            nowyPracownik.setHaslo(noweHaslo.getText());
            nowyPracownik.setLogin(nowyLogin.getText());
            nowyPracownik.setStanowisko((short)Integer.parseInt(nowaRola.getValue().toString()));
            nowyPracownik.setImie(noweImie.getText());
            nowyPracownik.setNazwisko(noweNazwisko.getText());
            session.save(nowyPracownik);
            session.getTransaction().commit();
            blad.setText("Stworzyłem użytkownika "+nowyLogin.getText());

            session.clear();
            session.disconnect();
            session.close();
        }catch(Exception e){
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }

    }

    public void usunUzytkownika() {
        System.out.println("Usunięto Użytkownika!");
    }

    public void usunZlecenie() {
        System.out.println("Usunieto zlecenie!");
    }

    public void usunCzesc() {
        System.out.println("Usunieto czesc!");
    }

    public void usunZamowienie() {
        if(zamowienia.getSelectionModel().isEmpty()) return;
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();

            Zamowienia zamowienie = (Zamowienia)zamowienia.getSelectionModel().getSelectedItem();
            System.out.println("PRZESZEDŁEM DALEJ!!!! "+zamowienie.getIdZamowienia()*30);
            session.delete(zamowienie);

            List<Zamowienia> zamowieniaTab =  session.createQuery("SELECT a FROM Zamowienia a", Zamowienia.class).getResultList();
            zamowienia.getItems().clear();
            for(Zamowienia z : zamowieniaTab){
                zamowienia.getItems().add(z);
            }

            session.getTransaction().commit();

            session.clear();
            session.disconnect();
            session.close();
        }catch(Exception e){
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        System.out.println("Usunięto zamówienie!");
    }


    public void inicjalizujWidokAdminaZBazy(){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            List<Pracownicy> pracownicy =  session.createQuery("SELECT a FROM Pracownicy a", Pracownicy.class).getResultList();
            imieColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
            nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
            loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
            rolaColumn.setCellValueFactory(new PropertyValueFactory<>("stanowisko"));
            for(Pracownicy p : pracownicy){
                uzytkownicy.getItems().add(p);
            }
            idColumn.setCellValueFactory(new PropertyValueFactory<>("idZamowienia"));
            mechanikColumn.setCellValueFactory(new PropertyValueFactory<>("pracownikImie"));
            komentarzColumn.setCellValueFactory(new PropertyValueFactory<>("komentarz"));
            stanColumn.setCellValueFactory(new PropertyValueFactory<>("stanZamowienia"));
            nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaCzesci"));
            List<Zamowienia> zamowieniaTab =  session.createQuery("SELECT a FROM Zamowienia a", Zamowienia.class).getResultList();
            for(Zamowienia z : zamowieniaTab){
                zamowienia.getItems().add(z);
            }
            nazwaCzesciColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaCzesci"));
            opisColumn.setCellValueFactory(new PropertyValueFactory<>("opisCzesci"));
            iloscColumn.setCellValueFactory(new PropertyValueFactory<>("ilosc"));
            cenaColumn.setCellValueFactory(new PropertyValueFactory<>("cena"));
            List<Magazyn> magazyn =  session.createQuery("SELECT a FROM Magazyn a", Magazyn.class).getResultList();
            for(Magazyn z : magazyn){
                czesci.getItems().add(z);
            }

            session.clear();
            session.disconnect();
            session.close();
        }catch(Exception e){
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}