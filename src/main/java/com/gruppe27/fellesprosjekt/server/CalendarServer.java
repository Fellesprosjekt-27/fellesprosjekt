package com.gruppe27.fellesprosjekt.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.gruppe27.fellesprosjekt.common.Network;
import com.gruppe27.fellesprosjekt.common.TestMessage;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.server.controllers.GroupController;

import java.io.IOException;
import java.sql.SQLException;

public class CalendarServer {
    Server server;
    DatabaseConnector connector;
    CalendarConnection connection;

    public CalendarServer() {
        connection = new CalendarConnection();
        server = new Server() {
            protected Connection newConnection() {
                return connection;
            }
        };

        Network.register(server);

        try {
            connector = new DatabaseConnector();
            connection.setDatabaseConnection(connector.getDatabaseConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        server.addListener(new CalendarListener(server, connection));

        try {
            server.bind(Network.PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        server.start();
    }


    public static void main(String[] args) {
        CalendarServer server = new CalendarServer();
    }
}
