package com.gruppe27.fellesprosjekt.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.gruppe27.fellesprosjekt.common.Network;
import com.gruppe27.fellesprosjekt.common.TestMessage;
import com.gruppe27.fellesprosjekt.common.User;

import java.io.IOException;

public class CalendarServer {
    Server server;

    public CalendarServer() throws IOException {
        server = new Server() {
            protected Connection newConnection() {
                return new CalendarConnection();
            }
        };

        Network.register(server);

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

        server.bind(Network.PORT);
        server.start();
    }

    public static void main(String[] args) throws IOException {
        CalendarServer server = new CalendarServer();
        DatabaseConnector databaseConnector = new DatabaseConnector();

        /*
        String username = "username";
        String name = "name";
        User user = new User(username,name);
        String password = "password";
        databaseConnector.registerUser(user, password);
        */

    }
}
