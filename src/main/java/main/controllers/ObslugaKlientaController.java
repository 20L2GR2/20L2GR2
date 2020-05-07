package main.controllers;

import hibernate.entity.Klienci;
import hibernate.entity.Pracownicy;
import hibernate.entity.Zlecenia;
import hibernate.util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ObslugaKlientaController implements Initializable {
    LogowanieController mainController = new LogowanieController();

    @FXML
    public Button buttonLogout;
    public ToggleButton toogleButtonUtworzZlecenie;
    public ToggleButton toogleButtonUkonczoneZlecenia;
    public ToggleButton toogleButtonTwojProfil;
    public Pane utworzZleceniePane;
    public Pane ukonczoneZleceniaPane;
    public Pane twojProfilPane;
    public BorderPane obslugaKlientaBorderPane;

    @FXML
    public TextField klientImie, klientNazwisko, klientTelefon, klientMarka, klientModel, klientRejestracja;

    @FXML
    public TextArea klientOpis;

    @FXML
    public Label bladKlient, imieLabel, nazwiskoLabel, loginLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obslugaKlientaBorderPane.setCenter(utworzZleceniePane);
        selectedButton(toogleButtonUtworzZlecenie);
        //inicjalizujWidokObslugiKlientaZBazy();
    }

    public void logout(ActionEvent event) throws IOException {
        mainController.logout(event);
    }

    public void otworzTwojProfil() {
        System.out.println("otworzTwojProfil");
        obslugaKlientaBorderPane.setCenter(twojProfilPane);
        selectedButton(toogleButtonTwojProfil);
        Pracownicy pracownik = null;
        inicjalizujWidokObslugiKlientaZBazy(pracownik);
    }

    public void otworzUkonczoneZlecenia() {
        System.out.println("otworzUkonczoneZlecenia");
        obslugaKlientaBorderPane.setCenter(ukonczoneZleceniaPane);
        selectedButton(toogleButtonUkonczoneZlecenia);
        Pracownicy pracownik = null;
        inicjalizujWidokObslugiKlientaZBazy(pracownik);
    }

    public void otworzUtworzZlecenie() {
        System.out.println("otworzUtworzZlecenia");
        obslugaKlientaBorderPane.setCenter(utworzZleceniePane);
        selectedButton(toogleButtonUtworzZlecenie);
        Pracownicy pracownik = null;
        inicjalizujWidokObslugiKlientaZBazy(pracownik);
    }

    private void selectedButton(ToggleButton button) {
        button.setSelected(true);
    }

    public void dodajZlecenieButton() throws ParseException {
        if (klientImie.getText().isEmpty()) {
            bladKlient.setText("Podano zle dane");
            return;
        }
        if (klientNazwisko.getText().isEmpty()) {
            bladKlient.setText("Podano zle dane");
            return;
        }
        if (klientTelefon.getText().isEmpty()) {
            bladKlient.setText("Podano zle dane");
            return;
        }
        if (klientMarka.getText().isEmpty()) {
            bladKlient.setText("Podano zle dane");
            return;
        }
        if (klientModel.getText().isEmpty()) {
            bladKlient.setText("Podano zle dane");
            return;
        }
        if (klientRejestracja.getText().isEmpty()) {
            bladKlient.setText("Podano zle dane");
            return;
        }

        Klienci klient = null;
        Pracownicy pracownik = new Pracownicy();
        isOrCreateRejestracjaInDb(klient);
        if(klient == null){
            createKlient(klient);
            System.out.println("Dodano Klienta");
            klient.setNrReje(klient.getNrReje());
            klient.setIdKlienta(klient.getIdKlienta());
        }
        inicjalizujWidokObslugiKlientaZBazy(pracownik);
        createZlecenie(klient,pracownik);
    }

    public void isOrCreateRejestracjaInDb(Klienci klient) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            klient = session.createQuery("SELECT a FROM Klienci a WHERE a.nrReje = :numer_rejestracyjny", Klienci.class).setParameter("numer_rejestracyjny", klientRejestracja.getText()).getSingleResult();
            klient.setIdKlienta(klient.getIdKlienta());
            klient.setImie(klient.getImie());
            klient.setNazwisko(klient.getNazwisko());
            klient.setNrKontakt((int) klient.getNrKontakt());
            klient.setMarka(klient.getMarka());
            klient.setModel(klient.getModel());
            klient.setNrReje(klient.getNrReje());
            session.clear();
            session.disconnect();
            session.close();
        }catch(NoResultException nre){
            if(transaction != null) transaction.rollback();
            return;
        }catch (Exception e){
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void createKlient(Klienci nowyKlient) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            nowyKlient.setImie(klientImie.getText());
            nowyKlient.setNazwisko(klientNazwisko.getText());
            nowyKlient.setNrKontakt(Integer.parseInt(klientTelefon.getText()));
            nowyKlient.setMarka(klientMarka.getText());
            nowyKlient.setModel(klientModel.getText());
            nowyKlient.setNrReje(klientRejestracja.getText());

            session.save(nowyKlient);
            session.getTransaction().commit();
            bladKlient.setText("Dodano Klienta");
            session.clear();
            session.disconnect();
            session.close();
        }catch (Exception e){
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void createZlecenie(Klienci klient, Pracownicy pracownik) throws ParseException {
        Zlecenia noweZlecenie = new Zlecenia();
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

            noweZlecenie.setKlientZlecenie(klient);
            noweZlecenie.setDataRozpoczecia(timestamp);
            //noweZlecenie.setDataRozpoczecia(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(timestamp)));
            noweZlecenie.setStanZlecenia(0);
            noweZlecenie.setPracownikObslugaStart(pracownik);
            noweZlecenie.setOpisUsterki(klientOpis.getText());

            session.save(noweZlecenie);
            session.getTransaction().commit();
            session.clear();
            session.disconnect();
            session.close();
        }catch (Exception e){
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void inicjalizujWidokObslugiKlientaZBazy(Pracownicy pracownik) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            pracownik = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.idPracownika = :id").setParameter("id", LogowanieController.userID).uniqueResult();
            pracownik.setIdPracownika(pracownik.getIdPracownika());
            pracownik.setStanowisko(pracownik.getStanowisko());
            pracownik.setImie(pracownik.getImie());
            pracownik.setNazwisko(pracownik.getNazwisko());
            pracownik.setLogin(pracownik.getLogin());
            pracownik.setHaslo(pracownik.getHaslo());

            imieLabel.setText(pracownik.getImie());
            nazwiskoLabel.setText(pracownik.getNazwisko());
            loginLabel.setText(pracownik.getLogin());
            session.clear();
            session.disconnect();
            session.close();
        }catch (Exception e){
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void wybierzZlecenieButton() {
        System.out.println("Wybrano zlecenie button");
    }

    public void destroySession(Session session) {
        session.clear();
        session.disconnect();
        session.close();
        HibernateUtil.shutdown();
    }
}
