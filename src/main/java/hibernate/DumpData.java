package hibernate;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Klasa potrzebna do testow jednostkowych na bazie danych
 */

public class DumpData {

    /**
     * Metoda ladujaca dane do bazy danych z pliku.
     */

    public static void dumpDataToDatabase() {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            //Registering the Driver
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            //Getting the connection
            String mysqlUrl = "jdbc:mysql://localhost/20l2gr2";
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "");
            System.out.println("Connection established......");
            //Initialize the script runner
            ScriptRunner sr = new ScriptRunner(con);
            //Creating a reader object
            Reader reader = new BufferedReader(new FileReader("dump.sql"));
            //Running the script
            sr.runScript(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda czyszczaca wszystkie rekordy w bazie danych.
     */

    public static void deleteDataFromDatabase() {
        try {
            //Registering the Driver
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            //Getting the connection
            String mysqlUrl = "jdbc:mysql://localhost/20l2gr2";
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "");
            System.out.println("Connection established......");
            //Initialize the script runner
            ScriptRunner sr = new ScriptRunner(con);
            //Creating a reader object
            Reader reader = new BufferedReader(new FileReader("delete.sql"));
            //Running the script
            sr.runScript(reader);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
