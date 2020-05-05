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

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    static int zmienna = 0;
    static int zmienna2 = 0;

    @FXML
    public TextField klientImie, klientNazwisko, klientTelefon, klientMarka, klientModel, klientRejestracja;

    @FXML
    public TextArea klientOpis;

    @FXML
    public Label bladKlient, imieLabel, nazwiskoLabel, loginLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obslugaKlientaBorderPane.setCenter(utworzZleceniePane);
        toogleButtonUtworzZlecenie.setSelected(true);
        //inicjalizujWidokObslugiKlientaZBazy();
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

    public void dodajZlecenieButton() {
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
        if (klientOpis.getText().isEmpty()) {
            bladKlient.setText("Podano zle dane");
            return;
        }
        Transaction transactionKlient = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (!nrRejeInDb(session)) {
                System.out.println("Tworzę klienta!");
                createKlient(session);
            }
            Klienci klient = new Klienci();
            klient.setIdKlienta(zmienna);
            Pracownicy pracownik = new Pracownicy();
            inicjalizujWidokObslugiKlientaZBazy();
            pracownik.setIdPracownika(zmienna2);

            Zlecenia noweZlecenie = new Zlecenia();

            //DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            //Date dateobj = new Date();
            //java.sql.Timestamp sqlTime=new java.sql.Timestamp(date.getTime());
            //java.util.Date date=new java.util.Date();
            //java.sql.Timestamp sqlTime=new java.sql.Timestamp(date.getTime());

            //Klienci zmienna1 = zmienna;

            noweZlecenie.setKlientZlecenie(klient);
            noweZlecenie.setOpisUsterki(klientOpis.getText());
            //noweZlecenie.setDataRozpoczecia(sqlTime);
            noweZlecenie.setPracownikObslugaStart(pracownik);
            session.save(noweZlecenie);
            session.getTransaction().commit();


            destroySession(session);
        } catch (Exception e) {
            if (transactionKlient != null) transactionKlient.rollback();
            e.printStackTrace();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (zmienna != 0) nrRejeInDb(session);
        } catch (Exception e) {
            if (transactionKlient != null) transactionKlient.rollback();
            e.printStackTrace();
        }
        zmienna = 0;
        zmienna2 = 0;
    }

    public boolean nrRejeInDb(Session session) {
        Transaction transaction = session.beginTransaction();
        Klienci user = new Klienci();
        try {
            user = (Klienci) session.createQuery("SELECT a FROM Klienci a WHERE a.nrReje = :numer_rejestracyjny", Klienci.class).setParameter("numer_rejestracyjny", klientRejestracja.getText()).getSingleResult();
            zmienna = user.getIdKlienta();
            if (user != null) {
                bladKlient.setText("Taki numer rejestracyjny jest w bazie!");
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createKlient(Session session){
        Klienci nowyKlient = new Klienci();
        String s_tel = klientTelefon.getText();
        nowyKlient.setImie(klientImie.getText());
        nowyKlient.setNazwisko(klientNazwisko.getText());
        try {
            int i = Integer.parseInt(s_tel.trim());
            nowyKlient.setNrKontakt(i);
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException: " + nfe.getMessage());
        }
        nowyKlient.setMarka(klientMarka.getText());
        nowyKlient.setModel(klientModel.getText());
        nowyKlient.setNrReje(klientRejestracja.getText());

        session.save(nowyKlient);
        session.getTransaction().commit();
        bladKlient.setText("Dodano zlecenie.");
    }

    public void destroySession(Session session){
        session.clear();
        session.disconnect();
        session.close();
        HibernateUtil.shutdown();
    }

    public void wybierzZlecenieButton() {
        System.out.println("Wybrano zlecenie button");
    }

    public void inicjalizujWidokObslugiKlientaZBazy() {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Pracownicy user = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.idPracownika = :id").setParameter("id", LogowanieController.userID).uniqueResult();
            zmienna2 = user.getIdPracownika();
//            String cos = String.valueOf(LogowanieController.userID);

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
