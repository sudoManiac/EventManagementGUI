package sample;
import java.sql.*;

public class ConnectDB {
    private static Connection con = null;
    public static Statement connect(String database) {
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database,"root","mysql");
            Statement stmt = con.createStatement();
            return stmt;
        }
        catch (Exception e){
            System.out.println("Some Error Occured : Could not Connect") ;
            e.printStackTrace();
            Statement stmt = null;
            return stmt;
        }
    }

    public static void end(){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
