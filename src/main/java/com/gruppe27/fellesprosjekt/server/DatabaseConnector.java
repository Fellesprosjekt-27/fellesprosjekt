package com.gruppe27.fellesprosjekt.server;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConnector {
    private static Connection connection = null;

    protected DatabaseConnector() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            Properties properties = new Properties();
            try {
                InputStream inputStream = DatabaseConnector.class.getClassLoader().getResourceAsStream("config.properties");
                if (inputStream != null) {
                    properties.load(inputStream);
                } else {
                    System.out.println("Couldn't find config.properties");
                    System.exit(1);
                }

                connection = DriverManager.getConnection(properties.getProperty("DB_URL"),
                        properties.getProperty("DB_USER"), properties.getProperty("DB_PASSWORD"));
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return connection;
    }
}
