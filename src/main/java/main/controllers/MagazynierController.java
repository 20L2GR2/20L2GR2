package main.controllers;

import hibernate.entity.Magazyn;
import hibernate.entity.Pracownicy;
import hibernate.entity.Zamowienia;
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
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
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
    // tableMagazyn
    public TableColumn magazynNazwaCzesciColumn, magazynOpisCzesciColumn, magazynIloscCzesciColumn, magazynCenaCzesciColumn;
    private TableColumn<Magazyn, String> nazwaCzesciColumn, opisColumn;
    private TableColumn<Magazyn, Long> iloscColumn;
    private TableColumn<Magazyn, Float> cenaColumn;
    // tableZamowienia
    public TableColumn zamowieniaNazwaCzesciColumn, zamowieniaKomentarzColumn, zamowieniaMechanikColumn;
    @FXML
    public Label imieLabel, nazwiskoLabel, loginLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
        //inicjalizujWidokMagazynieraZBazy();
    }

    public void logout(ActionEvent event) throws IOException {
        mainController.logout(event);
    }

    public void otworzZamowienia() {
        System.out.println("otworzZamowienia");
        borderPane.setCenter(zamowieniaPane);
        toggleButtonZamowienia.setSelected(true);
        inicjalizujWidokMagazynieraZBazy();
    }

    public void otworzStanMagazyn() {
        System.out.println("otworzStanMagazynu");
        borderPane.setCenter(stanMagazynuPane);
        toggleButtonStanmagazyn.setSelected(true);
        inicjalizujWidokMagazynieraZBazy();
    }

    public void otworzProfil() {
        System.out.println("otworzProfil");
        borderPane.setCenter(profilPane);
        toggleButtonProfil.setSelected(true);
        inicjalizujWidokMagazynieraZBazy();
    }

    public void dodajCzesc() {
        System.out.println("Dodano!");
    }

    public void usunCzesc() {
        System.out.println("Usunięto!");
    }

    public void usunZlecenie() {
        System.out.println("Usunięto!");
    }

    private void updateData(Object object) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("UPDATE!");
            session.update(object);
            session.getTransaction().commit();

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

            List<Zamowienia> zamowienia = session.createQuery("SELECT z FROM Zamowienia z WHERE z.stanZamowienia = 0", Zamowienia.class).getResultList();

            zamowieniaNazwaCzesciColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaCzesci"));
            zamowieniaKomentarzColumn.setCellValueFactory(new PropertyValueFactory<>("komentarz"));
            zamowieniaMechanikColumn.setCellValueFactory(new PropertyValueFactory<>("imieNazwisko"));

            for (Zamowienia z : zamowienia) {
                tableZamowienia.getItems().add(z);
            }

            //------------------------------------------------------------

            List<Magazyn> magazyn = session.createQuery("SELECT a FROM Magazyn a", Magazyn.class).getResultList();

            magazynNazwaCzesciColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaCzesci"));
            magazynOpisCzesciColumn.setCellValueFactory(new PropertyValueFactory<>("opisCzesci"));
            magazynIloscCzesciColumn.setCellValueFactory(new PropertyValueFactory<>("ilosc"));
            magazynCenaCzesciColumn.setCellValueFactory(new PropertyValueFactory<>("cena"));

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