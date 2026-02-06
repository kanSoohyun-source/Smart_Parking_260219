package org.example.smart_parking_260219.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection_hjs {
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection = null;
        String url = "jdbc:mariadb://localhost:3308/sample";
        String user = "root";
        String pass = "1642";

        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, pass);
        return connection;
    }
}
