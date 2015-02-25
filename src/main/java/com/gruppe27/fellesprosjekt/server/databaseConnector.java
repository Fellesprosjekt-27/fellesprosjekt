package com.gruppe27.fellesprosjekt.server;

import com.gruppe27.fellesprosjekt.common.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    static final String DB_URL = "jdbc:mysql://mysql.stud.ntnu.no/andreahd_caltest";
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