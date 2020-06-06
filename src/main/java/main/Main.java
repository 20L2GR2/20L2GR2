package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.apache.ibatis.jdbc.ScriptRunner;
//import pdf.GeneratePdf;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws SQLException, FileNotFoundException {
//        //GENEROWANIE PDF odbywa się za pomocą tej klasy
//        GeneratePdf pdf = new GeneratePdf();
//        String[][] koszta = {{"Naprawa silnika", "200.50"},{"Szpachla", "100.75"}};
//        pdf.generatePDF("pdf/pdf.pdf", new Date().toString(),"Dobra-Firma", "Roafał Kowalski", "Warszawa szkolna 2", koszta);
//        //

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/application.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 1600, 800);


        stage.setTitle("AutoService");
        stage.setScene(scene);
        stage.show();
        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setAutomaticallyColorPanes(true);
        jMetro.setScene(scene);
        root.setStyle("-fx-font: title");

        //Registering the Driver
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        //Getting the connection
        String mysqlUrl = "jdbc:mysql://localhost/20l2gr2";
        Connection con = DriverManager.getConnection(mysqlUrl, "root", "root");
        System.out.println("Connection established......");
        //Initialize the script runner
        ScriptRunner sr = new ScriptRunner(con);
        //Creating a reader object
        Reader reader = new BufferedReader(new FileReader("dump.sql"));
        //Running the script
        sr.runScript(reader);

    }


}