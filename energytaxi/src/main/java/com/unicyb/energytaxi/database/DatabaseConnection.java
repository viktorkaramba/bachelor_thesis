package com.unicyb.energytaxi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection initializeDatabase()
            throws SQLException, ClassNotFoundException
    {
        String DRIVER = "oracle.jdbc.driver.OracleDriver";
        String dbURL = "jdbc:oracle:thin:@host.docker.internal:1521:xe";
        String dbUsername = "System";
        String dbPassword = "karamba614";
        Class.forName(DRIVER);
        Connection con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
        return con;
    }
}
