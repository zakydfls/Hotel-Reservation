/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

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
public class Rooms {
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
            model.addColumn("Kode Kamar");
            model.addColumn("Nomor Kamar");
            model.addColumn("Tipe Kamar");
            model.addColumn("Harga Kamar");
 
            stmt = conn.createStatement();
            String sql = "SELECT * FROM rooms";
            int i = 1;
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
            	model.addRow(new Object[] {
                        rs.getInt("id"),
            		rs.getString("room_code"),
            		rs.getString("room_number"),
            		rs.getString("room_type"),
            		rs.getInt("price"),
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
            PreparedStatement statement = conn.prepareStatement("SELECT id FROM rooms WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }        
    public void update(String newKode, String newNomor, String newTipe, double newHarga, int id) {
	try {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL,USER,PASS);
                boolean exists = checkIDExists(id);
                if(exists){
                        ps = conn.prepareStatement("UPDATE rooms SET room_code=?, room_number=?, room_type=?, price=? WHERE id=?");
                        ps.setString(1, newKode);
	                ps.setString(2, newNomor);
	                ps.setString(3, newTipe);
	                ps.setDouble(4, newHarga);
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
                        ps = conn.prepareStatement("DELETE FROM rooms WHERE id=?");
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
    public void insert(String kode, String nomor, String tipe, double harga)
		{
			try {
                            Class.forName(JDBC_DRIVER);
                            conn = DriverManager.getConnection(DB_URL,USER,PASS);
                            stmt = conn.createStatement();
                            if (isKodeRoomExist(conn, kode)) {
                            JOptionPane.showMessageDialog(null, "Insert data gagal, kode rooms sudah dipakai!");
                            } else {
				
				String sql = "INSERT INTO rooms (room_code,room_number,room_type,price) VALUES (?,?,?,?)";
				ps = conn.prepareStatement(sql);
				
				ps.setString(1, kode);
				ps.setString(2, nomor);
				ps.setString(3, tipe);
				ps.setDouble(4, harga);
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
    private static boolean isKodeRoomExist(Connection conn, String koderoom) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {
            stmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM rooms WHERE room_code = ?");
            stmt.setString(1, koderoom);
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
}
