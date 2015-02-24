package com.gruppe27.fellesprosjekt.server;

import com.gruppe27.fellesprosjekt.common.User;

import java.sql.*;

public class DatabaseConnector {
    //JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://mysql.stud.ntnu.no/andreahd_caltest";

    static final String USER = "andreahd";
    static final String PASS = "gurgle1";

    public Statement connectToDatabase() {
        //STEP 3: Open a connection
        System.out.println("Connecting to database...");
        try {
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            return conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection to database failed.");
        }

        return null;
    }


    public boolean registerUser(User user, String password) {
        String userSql;
        userSql = "INSERT INTO ACCOUNT (username, personname, password)" +
                " VALUES ('" + user.getUsername() + "','" + user.getName() + "','" + password + "');";
        try {
            Statement stmt = connectToDatabase();
            stmt.executeUpdate(userSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



}
