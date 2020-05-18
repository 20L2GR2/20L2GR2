package main.controllers;

import hibernate.entity.Klienci;
import hibernate.entity.Pracownicy;
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
import java.text.ParseException;
import java.util.List;
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
    private TextField klientImie, klientNazwisko, klientTelefon, klientMarka, klientModel, klientRejestracja, kwotaUslugi, szukajZlecenia;
    ;

    @FXML
    private TextArea klientOpis;

    @FXML
    private Label bladKlient, imieLabel, nazwiskoLabel, loginLabel, bladUkonczone, mechanikLabel, obslugaLabel, markaLabel, opisUsterkiLabel, opisNaprawyLabel, uzyteCzesciLabel;

    @FXML
    private TableView tableUkonczone;

    @FXML
    private TableColumn imieNazwiskoColumn, nrRejeColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obslugaKlientaBorderPane.setCenter(twojProfilPane);
        selectedButton(toogleButtonTwojProfil);
        inicjalizujWidokObslugiKlientaZBazy();

        // only numbers and . ,
        kwotaUslugi.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[1-9]*\\d?(.\\d{1,2})*")) return;
            kwotaUslugi.setText(newValue.replaceAll("[^[1-9]*\\d?(.\\d{1,2})*]", ""));
        });

        szukajZlecenia.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                tableUkonczone.getItems().clear();
                if (!t1.isEmpty()) {
                    filtering(t1);
                } else inicjalizujWidokObslugiKlientaZBazy();
            }
        });
    }

    private void filtering(String text) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            List<Zlecenia> zlecenia = session.createQuery("SELECT z FROM Zlecenia z WHERE z.stanZlecenia = :stan", Zlecenia.class).setParameter("stan", 2).getResultList();

            for (Zlecenia z : zlecenia) {
                if (z.getNrReje().toUpperCase().contains(text.toUpperCase())) {
                    tableUkonczone.getItems().add(z);
                }
            }
            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
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
        if (klient == null) {
            klient = createKlient();
        }
        createZlecenie(klient, pracownik);
    }

    public Klienci isOrCreateRejestracjaInDb() {
        Klienci klient = new Klienci();
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            klient = session.createQuery("SELECT a FROM Klienci a WHERE a.nrReje = :numerRejestracyjny AND a.imie = :imie AND a.nazwisko = :nazwisko", Klienci.class).setParameter("numerRejestracyjny", klientRejestracja.getText()).setParameter("imie", klientImie.getText()).setParameter("nazwisko", klientNazwisko.getText()).getSingleResult();
            session.clear();
            session.disconnect();
            session.close();
            return klient;
        } catch (Exception e) {
            //if(transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public Klienci createKlient() {
        Klienci nowyKlient = new Klienci();
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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
        } catch (Exception e) {
            //if(transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public void createZlecenie(Klienci klient, Pracownicy pracownik) throws ParseException {
        Zlecenia noweZlecenie = new Zlecenia();
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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
        } catch (Exception e) {
            //if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Pracownicy inicjalizujWidokObslugiKlientaZBazy() {
        tableUkonczone.getItems().clear();
        Transaction transaction = null;
        Pracownicy pracownik = new Pracownicy();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            pracownik = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.idPracownika = :id").setParameter("id", LogowanieController.userID).getSingleResult();

            System.out.println("Przed");

            List<Zlecenia> zlecenia = session.createQuery("SELECT z FROM Zlecenia z WHERE z.stanZlecenia = :stan", Zlecenia.class).setParameter("stan", 2).getResultList();

            System.out.println(zlecenia);

            imieNazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("imieNazwisko"));
            nrRejeColumn.setCellValueFactory(new PropertyValueFactory<>("nrReje"));

            for (Zlecenia z : zlecenia) {
                tableUkonczone.getItems().add(z);
            }

            System.out.println("Po");

            imieLabel.setText(pracownik.getImie());
            nazwiskoLabel.setText(pracownik.getNazwisko());
            loginLabel.setText(pracownik.getLogin());
            session.clear();
            session.disconnect();
            session.close();
            return pracownik;
        } catch (Exception e) {
            //if(transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public void wybierzZlecenieButton() {
        if (tableUkonczone.getSelectionModel().isEmpty()) {
            bladUkonczone.setText("Nie wybrano klienta!");
            return;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Zlecenia zlecenia = (Zlecenia) tableUkonczone.getSelectionModel().getSelectedItem();
            Zlecenia z = (Zlecenia) session.createQuery("SELECT z FROM Zlecenia z WHERE z.klientZlecenie.idKlienta = :id", Zlecenia.class).setParameter("id", zlecenia.getIdKlienta()).uniqueResult();
            System.out.println(z);
            mechanikLabel.setText(z.getImieNazwiskoMechanik());
            obslugaLabel.setText(z.getImieNazwiskoObslugaPoczatek());
            markaLabel.setText(z.getMarkaModel());
            opisUsterkiLabel.setText(z.getOpisUsterki());
            opisNaprawyLabel.setText(z.getOpisNaprawy());
            uzyteCzesciLabel.setText(z.getUzyteCzesci());

            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            //if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void zakonczZlecenieButton() {
        bladUkonczone.setStyle("-fx-text-fill: red");
        if (tableUkonczone.getSelectionModel().isEmpty()) {
            bladUkonczone.setText("Nie wybrano klienta!");
            return;
        }
        if (mechanikLabel.getText().trim().isEmpty()) {
            bladUkonczone.setText("Nie pobrano informacji!");
        }
        if (obslugaLabel.getText().trim().isEmpty()) {
            bladUkonczone.setText("Nie pobrano informacji!");
        }
        if (markaLabel.getText().trim().isEmpty()) {
            bladUkonczone.setText("Nie pobrano informacji!");
        }
        if (mechanikLabel.getText().trim().isEmpty()) {
            bladUkonczone.setText("Nie pobrano informacji!");
        }
        if (opisUsterkiLabel.getText().trim().isEmpty()) {
            bladUkonczone.setText("Nie pobrano informacji!");
        }
        if (opisNaprawyLabel.getText().trim().isEmpty()) {
            bladUkonczone.setText("Nie pobrano informacji!");
        }
        if (uzyteCzesciLabel.getText().trim().isEmpty()) {
            bladUkonczone.setText("Nie pobrano informacji!");
        }
        if (kwotaUslugi.getText().trim().isEmpty()) {
            bladUkonczone.setText("Nie podano kwoty!");
        }

        Float num = null;
        boolean numeric = true;
        try {
            num = Float.parseFloat(kwotaUslugi.getText());
        } catch (NumberFormatException e) {
            numeric = false;
        }

        if (!numeric) {
            bladUkonczone.setText("Niepoprawna cena części");
            return;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Pracownicy pracownik = new Pracownicy();
            pracownik.setIdPracownika(LogowanieController.userID);
            Zlecenia zlecenia = (Zlecenia) tableUkonczone.getSelectionModel().getSelectedItem();
            Zlecenia kwota = (Zlecenia) session.createQuery("SELECT z FROM Zlecenia z WHERE z.klientZlecenie.idKlienta = :id", Zlecenia.class).setParameter("id", zlecenia.getIdKlienta()).uniqueResult();
            kwota.setCena(num);
            kwota.setStanZlecenia(3);
            kwota.setPracownikObslugaKoniec(pracownik);

            java.util.Date date = new java.util.Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

            kwota.setDataZakonczenia(timestamp);

            System.out.println(kwota);
            session.saveOrUpdate(kwota);
            session.getTransaction().commit();

            bladUkonczone.setStyle("-fx-text-fill: white");
            bladUkonczone.setText("Zlecenie zakończone.");
            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            //if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

    }

    public void destroySession(Session session) {
        session.clear();
        session.disconnect();
        session.close();
        HibernateUtil.shutdown();
    }
}
