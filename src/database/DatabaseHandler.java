
package database;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;

public class DatabaseHandler {
    
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String CONN = 
        "jdbc:mysql://localhost:3306/library";
//    public static Connection conn;
        
    public static Connection getMySqlConnection() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = (Connection) DriverManager.getConnection(CONN, USERNAME, PASSWORD);
        return connection;
    }   
}