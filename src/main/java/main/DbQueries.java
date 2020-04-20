package main;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbQueries {
    MysqlConnect mysqlConnect = new MysqlConnect();
    List<String> dbTables = new ArrayList<String>();

    public void tablesToList(){
        dbTables.clear();
        String sql = "SHOW TABLES";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next() ){
                dbTables.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
    }

    public void clearDB(){
        tablesToList();
//        Iterator i = tables.iterator();
        String sql = "DELETE FROM ";

        int len = dbTables.size();
        try {
            //while (i.hasNext()) {i.next()
            for(short i = 0; i < len; i++){
                System.out.println(dbTables.get(i));
                PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql + dbTables.get(i)+";");
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
        System.out.println("!!!Wyczys###zczono!!!");
    }

    public void loadDbDump(){
        final String dbUser = "root";
        final String dbPass = "root";
        final String dbName = "20l2gr2";
        final String mysqlLocation = "C:\\xampp\\mysql\\bin\\";                                                         // Full Path to mysql executable -> if needed add this variable to exec
        URL resource = getClass().getResource("/db/20l2gr2.sql");                                                // Path & File for db data\dump
        tablesToList();
        System.out.println("So Far So good!");
        try{
            mysqlConnect.connect();
            Runtime.getRuntime().exec(/*mysqlLocation+*/"mysql -u "+dbUser+ " -p"+dbPass+" "+dbName+" < " + resource);  // uncomment the 'mysqlLocation'
            // String[] executeCmd = new String[]{mysqlLocation+"mysql -u " + dbUser+ " -p"+dbPass+" " + dbName+ " < " + resource};
            /*

//            File backupFile = new File(String.valueOf(resource));
//
//            //String[] command = new String[]{"mysql ", "-uroot", "-p", "20l2gr2"};
//            String[] command = new String[]{"mysql ", "-–user=" + dbUser, " -–password=" + dbPass, " -e", "source " + resource};
//
//            ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList(command));
//            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
//            processBuilder.redirectInput(ProcessBuilder.Redirect.from(backupFile));
//
//            Process process = processBuilder.start();
//            int processComplete = process.waitFor();

            */
//            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
//            int processComplete = runtimeProcess.waitFor();
//            if(processComplete == 0){
//                System.out.println("OK");
//            }else{
//                System.out.println("FUCKED UP");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            mysqlConnect.disconnect();
            System.out.println("!DONE!");
        }
    }
}
