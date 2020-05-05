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

public class MagazynierController implements Initializable {
    LogowanieController mainController = new LogowanieController();

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
    public TableColumn zamowieniaNazwaCzesciColumn, zamowieniaKomentarzColumn, zamowieniaMechanikColumn;
    @FXML
    public Label imieLabel, nazwiskoLabel, loginLabel;
    @FXML
    public TextField nowaNazwaCzesci, nowaOpisCzesci, nowaIloscCzesci, nowaCenaCzesci;
    @FXML
    public Label blad, usuwanieCzesciLabel, bladRealizacji;

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

    }

    public void logout(ActionEvent event) throws IOException {
        mainController.logout(event);
    }

    public void otworzZamowienia() {
        System.out.println("otworzZamowienia");
        borderPane.setCenter(zamowieniaPane);
        toggleButtonZamowienia.setSelected(true);
        bladRealizacji.setText("");
        inicjalizujWidokMagazynieraZBazy();
    }

    public void otworzStanMagazyn() {
        System.out.println("otworzStanMagazynu");
        borderPane.setCenter(stanMagazynuPane);
        toggleButtonStanmagazyn.setSelected(true);
        blad.setText("");
        inicjalizujWidokMagazynieraZBazy();
    }

    public void otworzProfil() {
        System.out.println("otworzProfil");
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
        inicjalizujWidokMagazynieraZBazy();
    }

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

            System.out.println("Dodawanie części...");
            Magazyn nowaCzesc = new Magazyn();
            nowaCzesc.setNazwaCzesci(nowaNazwaCzesci.getText());
            nowaCzesc.setOpisCzesci(nowaOpisCzesci.getText());
            nowaCzesc.setIlosc(Integer.parseInt(nowaIloscCzesci.getText()));
            nowaCzesc.setCena(num);
            session.save(nowaCzesc);
            tableMagazyn.getItems().add(nowaCzesc);
            session.getTransaction().commit();
            System.out.println("Dodawano!");

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


    public void usunCzesc() {

        usuwanieCzesciLabel.setStyle("-fx-text-fill: red;");

        if (tableMagazyn.getSelectionModel().isEmpty()) {
            usuwanieCzesciLabel.setText("Nie wybrano części");
            return;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Magazyn usuwanieCzesci = (Magazyn) tableMagazyn.getSelectionModel().getSelectedItem();
            System.out.println("Usuwanie części...");
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

    public void zrealizowaneButton() {
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
            zamowienie.setStanZamowienia((short) 2);
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
        System.out.println("Zamówienie zrealizowane");
    }

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

            List<Magazyn> magazyn = session.createQuery("SELECT a FROM Magazyn a", Magazyn.class).getResultList();

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


}