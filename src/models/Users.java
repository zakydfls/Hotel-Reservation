/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
import java.sql.*;
/**
 *
 * @author Zaky Dafalas
 */
public class Users {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	    static final String DB_URL = "jdbc:mysql://127.0.0.1/ta_pbo";
	    static final String USER = "root";
	    static final String PASS = "";
	    static Connection conn;
	    static Statement stmt;
	    static ResultSet rs;
	    static PreparedStatement ps;
            
 public boolean authenticateUser(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            rs = ps.executeQuery();

            boolean isValidUser = rs.next();

            rs.close();
            ps.close();
            conn.close();

            return isValidUser;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }           
}
