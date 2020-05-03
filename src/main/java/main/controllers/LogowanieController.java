package main.controllers;

import hibernate.entity.Pracownicy;
import hibernate.util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

public class LogowanieController {

    @FXML
    public TextField loginTextField;
    public PasswordField passwordField;
    public Label bladLogowaniaLabel;

    public void zaloguj(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        String login = loginTextField.getText();
        String haslo = passwordField.getText();
        int stanowisko = -1;

        try{
            if(validate(login,haslo)){
                Transaction transaction = null;
                Pracownicy user = null;
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    transaction = session.beginTransaction();
                    user = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.login = :login").setParameter("login",login).uniqueResult();
                    if ( user != null ) {
                        stanowisko = user.getStanowisko();
                        System.out.println(stanowisko);
                    }
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    e.printStackTrace();
                }
            }else{
                bladLogowaniaLabel.setVisible(true);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(stanowisko);

        switch (stanowisko){
            case 0:
                openWindow("/views/adminView.fxml", window);
                break;
            case 1:
                openWindow("/views/obslugaKlientaView.fxml", window);
                break;
            case 2:
                openWindow("/views/mechanikView.fxml", window);
                break;
            case 3:
                openWindow("/views/magazynierView.fxml", window);
                break;
            default:
                bladLogowaniaLabel.setVisible(true);
                break;
        }
    }

    public void logout(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        openWindow("/views/application.fxml", window);
    }

    private void openWindow(String name, Stage window) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(name));
        Scene scene = new Scene(parent,1600,800);

        window.setTitle("AutoService");
        window.setScene(scene);
        window.show();

        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setAutomaticallyColorPanes(true);
        jMetro.setScene(scene);
        parent.setStyle("-fx-font: title");
    }

    public boolean validate(String userName, String password) {
        Transaction transaction = null;
        Pracownicy user = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            user = (Pracownicy) session.createQuery("FROM Pracownicy U WHERE U.login = :login").setParameter("login",userName).uniqueResult();
            if (user != null && user.getHaslo().equals(password)) {
                return true;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }
}