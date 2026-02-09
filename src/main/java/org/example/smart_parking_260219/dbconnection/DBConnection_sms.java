package org.example.smart_parking_260219.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection_sms {
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection = null;

        String url = "jdbc:mariadb://localhost:3306/smart_parking_team2";
        String user = "root";
        String pass = "6569";

        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, pass);
        return connection;
    }
}
