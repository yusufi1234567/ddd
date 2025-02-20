package se.systementor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    String url = "jdbc:mysql://localhost:3306/northwind";
    String user = "root";
    String password = "hejsan123";


    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }

    public List<String> activeProducts(){
        ArrayList<String> products = new ArrayList<String>();

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ProductName FROM Products WHERE discontinued=0");

            while (rs.next()) {
                products.add(rs.getString("ProductName"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

}
