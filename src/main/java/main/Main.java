package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

//import pdf.GeneratePdf;

/**
 * <h1>AutoService</h1>
 * <p>Aplikacja wspomagajaca prace warsztatu samochodowego.</p>
 * Glowna klasa odpowiedzialna za uruchomienie programu.
 *
 * @author Albrycht Adrian
 * @author Bieda Andrzej
 * @author Borek Kamil
 * @author Cwynar Wiktor
 * @author Kluk Paweł
 * @version 1.0
 * @since 2020-06-08
 */


public class Main extends Application {

    /**
     * Glowna metoda w naszej aplikacji, ktora uruchamia program.
     * @param args Argument wymagany do uruchomienia okna w JavaFX.
     */

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Metoda wykorzystywana do uruchomienia sceny w JavaFX.
     * @param stage Odniesienie do zmiennej, ktora odnosi sie do klasy Stage odpowiedzialnej za uruchomienie sceny JavaFX.
     * @throws SQLException  Odniesienie do klasy odpowiedzialnej za zwrot obslugi bledu wyjatku.
     */

    @Override
    public void start(Stage stage) throws SQLException, FileNotFoundException {
        //GENEROWANIE PDF odbywa się za pomocą tej klasy
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


    }


}