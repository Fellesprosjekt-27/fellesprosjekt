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
    GroupController groupController;
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
        initializeControllers();

        server.addListener(new Listener() {
            public void received(Connection c, Object object) {
                CalendarConnection connection = (CalendarConnection) c;

                if (object instanceof TestMessage) {
                    System.out.println("Received a testmessage");
                    TestMessage received = (TestMessage) object;
                    TestMessage newMessage = new TestMessage("Received message: " + received.getMessage());

                    server.sendToAllTCP(newMessage);
                }
            }

            public void connected(Connection c) {
                CalendarConnection connection = (CalendarConnection) c;
                TestMessage sendMessage = new TestMessage("Hi!");
                connection.sendTCP(sendMessage);
            }
        });

        try {
            server.bind(Network.PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        server.start();
    }

    private void initializeControllers() {
        groupController = new GroupController(connection);
    }

    public static void main(String[] args) {
        CalendarServer server = new CalendarServer();
    }
}
