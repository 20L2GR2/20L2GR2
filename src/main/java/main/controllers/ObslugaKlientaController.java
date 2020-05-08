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
        obslugaKlientaBorderPane.setCenter(twojProfilPane);
        selectedButton(toogleButtonTwojProfil);
        inicjalizujWidokObslugiKlientaZBazy();
    }

    public void logout(ActionEvent event) throws IOException {
        mainController.logout(event);
    }

    public void otworzTwojProfil() {
        System.out.println("otworzTwojProfil");
        obslugaKlientaBorderPane.setCenter(twojProfilPane);
        selectedButton(toogleButtonTwojProfil);
        inicjalizujWidokObslugiKlientaZBazy();
    }

    public void otworzUkonczoneZlecenia() {
        System.out.println("otworzUkonczoneZlecenia");
        obslugaKlientaBorderPane.setCenter(ukonczoneZleceniaPane);
        selectedButton(toogleButtonUkonczoneZlecenia);
        inicjalizujWidokObslugiKlientaZBazy();
    }

    public void otworzUtworzZlecenie() {
        System.out.println("otworzUtworzZlecenia");
        obslugaKlientaBorderPane.setCenter(utworzZleceniePane);
        selectedButton(toogleButtonUtworzZlecenie);
        inicjalizujWidokObslugiKlientaZBazy();
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

        Klienci klient = isOrCreateRejestracjaInDb();
        Pracownicy pracownik = inicjalizujWidokObslugiKlientaZBazy();
        if(klient == null){
            klient = createKlient();
        }
        createZlecenie(klient,pracownik);
    }

    public Klienci isOrCreateRejestracjaInDb() {
        Klienci klient = new Klienci();
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            klient = session.createQuery("SELECT a FROM Klienci a WHERE a.nrReje = :numerRejestracyjny", Klienci.class).setParameter("numerRejestracyjny", klientRejestracja.getText()).getSingleResult();
            session.clear();
            session.disconnect();
            session.close();
            return klient;
        }catch (Exception e){
            //if(transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public Klienci createKlient() {
        Klienci nowyKlient = new Klienci();
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
            return nowyKlient;
        }catch (Exception e){
            //if(transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
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
            noweZlecenie.setStanZlecenia(0);
            noweZlecenie.setPracownikObslugaStart(pracownik);
            noweZlecenie.setOpisUsterki(klientOpis.getText());

            session.save(noweZlecenie);
            //session.getTransaction().commit();
            bladKlient.setText("Dodano Zlecenie");
            session.clear();
            session.disconnect();
            session.close();
        }catch (Exception e){
            //if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Pracownicy inicjalizujWidokObslugiKlientaZBazy() {
        Transaction transaction = null;
        Pracownicy pracownik = new Pracownicy();
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            pracownik = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.idPracownika = :id").setParameter("id", LogowanieController.userID).getSingleResult();

            imieLabel.setText(pracownik.getImie());
            nazwiskoLabel.setText(pracownik.getNazwisko());
            loginLabel.setText(pracownik.getLogin());
            session.clear();
            session.disconnect();
            session.close();
            return pracownik;
        }catch (Exception e){
            //if(transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
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
