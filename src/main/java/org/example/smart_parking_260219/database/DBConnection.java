package org.example.smart_parking_260219.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection = null;

        String url = "jdbc:mariadb://localhost:3306/smart_parking_team2";
        String user = "root";
        String pass = "0707";

        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, pass);
        return connection;
    }
}
