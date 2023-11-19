package carsharing.db.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/";
    static String DB_NAME = "CarSharing";
    static final String DELAY = ";DB_CLOSE_DELAY=-1";


    public static void setDatabaseFileName(String[] args) {
        if (args.length > 0 && args[0].equals("-databaseFileName")) {
            DB_NAME = args[1];
        }
    }

    public static void createDatabase(Statement stmt) {
        System.out.println("Connecting to database...");

        try {
            System.out.println("Creating table in given database...");


            String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(ID INT PRIMARY KEY AUTO_INCREMENT," +
                    " NAME VARCHAR(255) NOT NULL UNIQUE)";


            String carTableSQL = "CREATE TABLE IF NOT EXISTS  CAR " +
                    "(ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) NOT NULL UNIQUE, " +
                    "COMPANY_ID INT NOT NULL, " +
                    "CONSTRAINT FK_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID) ON DELETE SET NULL)";


            String customerTableSQL = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                    "(ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) NOT NULL UNIQUE, " +
                    "RENTED_CAR_ID INT, " +
                    "CONSTRAINT FK_CAR FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID) ON DELETE SET NULL)";
            //

            String alterTable = "ALTER TABLE COMPANY ALTER COLUMN ID RESTART WITH 1";

            stmt.execute(sql);
            stmt.execute(carTableSQL);
            stmt.execute(customerTableSQL);
            stmt.execute(alterTable);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL + DB_NAME + DELAY);
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}