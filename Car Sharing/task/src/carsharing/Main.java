package carsharing;

import java.sql.Connection;
import java.sql.Statement;

import static carsharing.db.Menus.Menus.mainMenuInput;
import static carsharing.db.Util.DatabaseUtil.*;

//The database file name is obtained from the command-line arguments
//Your database URL should work out to be like the following: jdbc:h2:./src/carsharing/db/{DATABASE_NAME}
public class Main {



    public static void main(String[] args){


        setDatabaseFileName(args);
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()){

            createDatabase(stmt);
            mainMenuInput(stmt,conn);

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }



    }






}