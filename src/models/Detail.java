/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import static models.UserManage.DB_URL;
import static models.UserManage.JDBC_DRIVER;
import static models.UserManage.PASS;
import static models.UserManage.USER;
import static models.UserManage.conn;

/**
 *
 * @author Zaky Dafalas
 */
public class Detail {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	    static final String DB_URL = "jdbc:mysql://127.0.0.1/ta_pbo";
	    static final String USER = "root";
	    static final String PASS = "";
	    static Connection conn;
	    static Statement stmt;
	    static ResultSet rs;
	    static PreparedStatement ps;
            
    public static ArrayList<String> retrieveLastInsertedData() {
        ArrayList<String> dataList = new ArrayList<>();

        try {
            Class.forName(JDBC_DRIVER);	   
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = conn.createStatement();
            String maxIdQuery = "SELECT MAX(id) AS max_id FROM transaksi";
            ResultSet maxIdResult = statement.executeQuery(maxIdQuery);
            int maxId = 0;
            if (maxIdResult.next()) {
                maxId = maxIdResult.getInt("max_id");
            }

            // Retrieve the last inserted data
            String query = "SELECT * FROM transaksi WHERE id = " + maxId;
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
//                int id = resultSet.getInt("id");
                String name = resultSet.getString("nama_cust");
                String hp = resultSet.getString("hp_cust");
                String kode = resultSet.getString("room_code");
                String tipe = resultSet.getString("room_type");
                String checkin = resultSet.getString("checkin");
                String checkout = resultSet.getString("checkout");
                String harga = resultSet.getString("harga_tot");

                // Add the retrieved data to the ArrayList
                dataList.add(name);
                dataList.add(hp);
                dataList.add(kode);
                dataList.add(tipe);
                dataList.add(checkin);
                dataList.add(checkout);
                dataList.add(harga);
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }
  }
