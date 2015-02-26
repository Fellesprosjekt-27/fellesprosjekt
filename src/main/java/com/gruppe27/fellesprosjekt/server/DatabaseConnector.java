package com.gruppe27.fellesprosjekt.server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnector {

    /*
    static final String DB_URL = "jdbc:mysql://localhost/fellesprosjekt";
    static final String USER = "root";
    static final String PASS = "";
    */
    static final String DB_URL = "jdbc:mysql://mysql.stud.ntnu.no/andreahd_cal";
    static final String USER = "andreahd";
    static final String PASS = "gurgle1";

    private Connection databaseConnection;

    public DatabaseConnector() throws SQLException {
        databaseConnection = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public Connection getDatabaseConnection() {
        return databaseConnection;
    }
}
