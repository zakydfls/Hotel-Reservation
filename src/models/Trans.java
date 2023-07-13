/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static models.Rooms.DB_URL;
import static models.Rooms.JDBC_DRIVER;
import static models.Rooms.PASS;
import static models.Rooms.USER;
import static models.Rooms.conn;
import static models.Rooms.ps;
import static models.Rooms.rs;
import static models.Rooms.stmt;

/**
 *
 * @author Zaky Dafalas
 */
public class Trans {
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
            model.addColumn("Nama");
            model.addColumn("No Hp");
            model.addColumn("Kode Kamar");
            model.addColumn("Tipe Kamar");
            model.addColumn("Checkin");
            model.addColumn("Checkout");
            model.addColumn("Total");
 
            stmt = conn.createStatement();
            String sql = "SELECT * FROM transaksi";
            int i = 1;
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
            	model.addRow(new Object[] {
                        rs.getInt("id"),
            		rs.getString("nama_cust"),
            		rs.getString("hp_cust"),
            		rs.getString("room_code"),
            		rs.getString("room_type"),
                        rs.getDate("checkin"),
                        rs.getDate("checkout"),
                        rs.getDouble("harga_tot"),
                        
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
    
    
    
    public void insert(String nama, String hp, String kode, String type, LocalDate checkInDate, LocalDate checkOutDate, double harga)
		{
			try {
                            Class.forName(JDBC_DRIVER);
                            conn = DriverManager.getConnection(DB_URL,USER,PASS);
                            stmt = conn.createStatement();
                            JDateChooser dateChooser = new JDateChooser();
                            Date selectedDate = dateChooser.getDate();			
				String sql = "INSERT INTO transaksi (nama_cust,hp_cust,room_code,room_type,checkin,checkout,harga_tot) VALUES (?,?,?,?,?,?,?)";
				ps = conn.prepareStatement(sql);
				
				ps.setString(1, nama);
				ps.setString(2, hp);
                                ps.setString(3, kode);
				ps.setString(4, type);
				ps.setObject(5, checkInDate.toString());
//                                ps.setDate(6, new java.sql.Date(checkOutDate.getTime()));
                                ps.setObject(6, checkOutDate.toString());
                                ps.setDouble(7, harga);
				ps.execute();
                                JOptionPane.showMessageDialog(null, "Pesanan berhasil dibuat!");
				
				stmt.close();
				conn.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		} 
        public double getHarga(String kode)
		{
                    int harga = 0;
			try {
                            Class.forName(JDBC_DRIVER);
                            conn = DriverManager.getConnection(DB_URL,USER,PASS);
                            stmt = conn.createStatement();		
				String sql = "SELECT harga FROM rooms WHERE rooms_code=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, kode);
                                rs = ps.executeQuery();
				if (rs.next()){
                                    harga = rs.getInt("price");
                                }
                                rs.close();
				stmt.close();
				conn.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
                        return harga;
		} 
    
    
//    private static boolean is(Connection conn, String koderoom) throws SQLException {
//        PreparedStatement stmt = null;
//        ResultSet resultSet = null;
//        try {
//            stmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM rooms WHERE room_code = ?");
//            stmt.setString(1, koderoom);
//            resultSet = stmt.executeQuery();
//            if (resultSet.next()) {
//                int count = resultSet.getInt("count");
//                return count > 0;
//            }
//        } finally {
//            if (resultSet != null) {
//                resultSet.close();
//            }
//            if (stmt != null) {
//                stmt.close();
//            }
//        }
//        return false;
//    }
}
