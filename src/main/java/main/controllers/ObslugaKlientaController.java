package main.controllers;

import hibernate.entity.Klienci;
import hibernate.entity.Magazyn;
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

import static java.lang.String.valueOf;

public class ObslugaKlientaController implements Initializable {
    LogowanieController mainController = new LogowanieController();

    List<Zlecenia> zlecenia;

    @FXML
    public Button buttonLogout;
    public ToggleButton toogleButtonUtworzZlecenie;
    public ToggleButton toogleButtonUkonczoneZlecenia;
    public ToggleButton toogleButtonHistoriaZlecen;
    public ToggleButton toogleButtonTwojProfil;
    public Pane utworzZleceniePane;
    public Pane ukonczoneZleceniaPane;
    public Pane twojProfilPane;
    public Pane historiaZlecenPane;
    public BorderPane obslugaKlientaBorderPane;

    @FXML
    public TextField klientImie, klientNazwisko, klientTelefon, klientMarka, klientModel, klientRejestracja, kwotaUslugi, szukajZlecenia;

    @FXML
    public TextArea klientOpis;

    @FXML
    public Label bladKlient, bladHistoria, imieLabel, nazwiskoLabel, loginLabel, bladUkonczone, mechanikLabel, obslugaLabel, markaLabel, opisUsterkiLabel, opisNaprawyLabel, uzyteCzesciLabel;
    public Label mechanikHistoria, obslugaPoczatekHistoria, obslugaKoniecHistoria, markaHistoria, opisUsterkiHistoria, opisNaprawyHistoria, uzyteCzesciHistoria, cenaHistoria, dataPoczatekHistoria, dataKoniecHistoria;
    @FXML
    public TableView tableUkonczone, tableHistoria;

    @FXML
    private TableColumn imieNazwiskoColumn, nrRejeColumn, nrRejeHistoria, imieNazwiskoHistoria;

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
                for (Zlecenia z : zlecenia) {
                    if (z.getNrReje().toUpperCase().contains(t1.toUpperCase())) {
                        tableUkonczone.getItems().add(z);
                    }
                }
            }
        });
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

    public void otworzHistoriaZlecen() {
        System.out.println("otworzHistoriaZlecen");
        obslugaKlientaBorderPane.setCenter(historiaZlecenPane);
        selectedButton(toogleButtonHistoriaZlecen);
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
            klient = session.createQuery("SELECT a FROM Klienci a WHERE a.nrReje = :numerRejestracyjny", Klienci.class).setParameter("numerRejestracyjny", klientRejestracja.getText()).getSingleResult();
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
        tableHistoria.getItems().clear();
        Transaction transaction = null;
        Pracownicy pracownik = new Pracownicy();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            pracownik = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.idPracownika = :id").setParameter("id", LogowanieController.userID).getSingleResult();

            System.out.println("Przed");

            zlecenia = session.createQuery("SELECT z FROM Zlecenia z WHERE z.stanZlecenia = :stan", Zlecenia.class).setParameter("stan", 2).getResultList();

            System.out.println(zlecenia);

            imieNazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("imieNazwisko"));
            nrRejeColumn.setCellValueFactory(new PropertyValueFactory<>("nrReje"));

            for (Zlecenia z : zlecenia) {
                tableUkonczone.getItems().add(z);
            }

            List<Zlecenia> historia = session.createQuery("SELECT h FROM Zlecenia h WHERE h.stanZlecenia = :stan", Zlecenia.class).setParameter("stan", 3).getResultList();

            System.out.println(historia);

            imieNazwiskoHistoria.setCellValueFactory(new PropertyValueFactory<>("imieNazwisko"));
            nrRejeHistoria.setCellValueFactory(new PropertyValueFactory<>("nrReje"));

            for (Zlecenia h : historia) {
                tableHistoria.getItems().add(h);
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

    public void wybierzHistorieButton() {
        if (tableHistoria.getSelectionModel().isEmpty()) {
            bladHistoria.setText("Nie wybrano klienta!");
            return;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Zlecenia zlecenia1 = (Zlecenia) tableHistoria.getSelectionModel().getSelectedItem();
            Zlecenia z1 = (Zlecenia) session.createQuery("SELECT z1 FROM Zlecenia z1 WHERE z1.klientZlecenie.idKlienta = :id", Zlecenia.class).setParameter("id", zlecenia1.getIdKlienta()).uniqueResult();
            System.out.println(z1);
            mechanikHistoria.setText(z1.getImieNazwiskoMechanik());
            obslugaPoczatekHistoria.setText(z1.getImieNazwiskoObslugaPoczatek());
            obslugaKoniecHistoria.setText(z1.getImieNazwiskoObslugaKoniec());
            markaHistoria.setText(z1.getMarkaModel());
            opisUsterkiHistoria.setText(z1.getOpisUsterki());
            opisNaprawyHistoria.setText(z1.getOpisNaprawy());
            uzyteCzesciHistoria.setText(z1.getUzyteCzesci());
            cenaHistoria.setText(valueOf(z1.getCena()));
            //dataPoczatekHistoria.setText(z1.getDataRozpoczecia());


            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            //if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void zakonczZlecenieButton() throws ParseException {
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
            //kwota.setPracownikObslugaKoniec(pracownik);

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
