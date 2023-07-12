/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Zaky Dafalas
 */
public class UserManage {
     static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	    static final String DB_URL = "jdbc:mysql://127.0.0.1/ta_pbo";
	    static final String USER = "root";
	    static final String PASS = "";
	    static Connection conn;
	    static Statement stmt;
	    static ResultSet rs;
	    static PreparedStatement ps;
    
    public DefaultTableModel showtable(){
        try{
            Class.forName(JDBC_DRIVER);	   
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Username");
            model.addColumn("Password");
            model.addColumn("Nama");
            model.addColumn("No Handphone");
 
            stmt = conn.createStatement();
            String sql = "SELECT * FROM users";
            int i = 1;
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String hash = rs.getString("password");
                String computedHash = computeHash(hash);
            	model.addRow(new Object[] {
                        rs.getInt("id"),
            		rs.getString("username"),
                        computedHash,
            		rs.getString("name"),
            		rs.getString("handphone"),
            	});
            	i++;
            }
            rs.close();
            conn.close();
            stmt.close();
            
            return model;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
        
    private boolean checkIDExists(int id) {
        boolean exists = false;
        try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            PreparedStatement statement = conn.prepareStatement("SELECT id FROM users WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }        
    public void update(String newUsername, String newPass, String newName, String newHp, int id) {
	try {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL,USER,PASS);
                boolean exists = checkIDExists(id);
                if(exists){
                        ps = conn.prepareStatement("UPDATE users SET username=?, password=?, name=?, handphone=? WHERE id=?");
                        ps.setString(1, newUsername);
	                ps.setString(2, newPass);
	                ps.setString(3, newName);
	                ps.setString(4, newHp);
	                ps.setInt(5, id);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
                }else{
                    JOptionPane.showMessageDialog(null, "Data dengan ID tersebut tidak ditemukan!");
                }
                stmt.close();
                conn.close();
		} catch(Exception e)
		{
		e.printStackTrace();
		}
	}
    public void delete(int id){
        try{
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            boolean exists = checkIDExists(id);
                if(exists){
                        ps = conn.prepareStatement("DELETE FROM users WHERE id=?");
	                ps.setInt(1, id);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                }else{
                    JOptionPane.showMessageDialog(null, "Data dengan ID tersebut tidak ditemukan!");
                }
            stmt.close();
            conn.close();
        }catch(Exception e)
		{
		e.printStackTrace();
		}
    }
    public void insert(String username, String password, String name, String hp)
		{
			try {
                            Class.forName(JDBC_DRIVER);
                            conn = DriverManager.getConnection(DB_URL,USER,PASS);
                            stmt = conn.createStatement();
                            if (isUsernameExist(conn, username)) {
                            JOptionPane.showMessageDialog(null, "Insert data gagal, kode rooms sudah dipakai!");
                            } else {
				
				String sql = "INSERT INTO users (username,password,name,handphone) VALUES (?,?,?,?)";
				ps = conn.prepareStatement(sql);
				
				ps.setString(1, username);
				ps.setString(2, password);
				ps.setString(3, name);
				ps.setString(4, hp);
				ps.execute();
                                JOptionPane.showMessageDialog(null, "Insert data berhasil!");
				
				stmt.close();
				conn.close();
                            }
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		} 
    private static boolean isUsernameExist(Connection conn, String username) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {
            stmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM users WHERE username = ?");
            stmt.setString(1, username);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return false;
    }
        private static String computeHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            // Convert the byte array to hexadecimal representation
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
