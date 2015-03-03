package com.gruppe27.fellesprosjekt.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.gruppe27.fellesprosjekt.common.Network;

import java.io.IOException;


public class CalendarServer {
    Server server;
    DatabaseConnector connector;

    public CalendarServer() {
        server = new Server() {
            protected Connection newConnection() {
                return new CalendarConnection();
            }
        };

        Network.register(server);

        server.addListener(new CalendarListener(server));

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
