
package addbook;

import database.DatabaseHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateTest {
    public static void main(String[] args) throws Exception {
        Connection conn = DatabaseHandler.getMySqlConnection();
//        PreparedStatement pst = conn.prepareStatement("insert into test (name) values (?)");
//            pst.setString(1, "John");
//            pst.execute();
//            pst.close();
            
//        String s = "mach";
//        PreparedStatement pst2 = conn.prepareStatement("update test set name = ? where id = ?");
//            pst2.setString(1, s);
//            pst2.setInt(2, 1);   
//            if(pst2.executeUpdate() == 1)
//                System.out.println("Success");
//            pst2.close();
            
            PreparedStatement pst = conn.prepareStatement("delete from test where id = ?");
            pst.setInt(1, 5);
            pst.execute();
            pst.close();
    }
}
