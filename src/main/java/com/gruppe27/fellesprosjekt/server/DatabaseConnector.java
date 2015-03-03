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

    private static Connection connection = null;

    protected DatabaseConnector() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return connection;
    }
}
