package com.gruppe27.fellesprosjekt.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.gruppe27.fellesprosjekt.common.Network;

import java.io.IOException;


public class CalendarServer {
    private static Server server = null;
    DatabaseConnector connector;

    protected CalendarServer() {
    }

    public static Server getServer() {
        if (server == null) {
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
        return server;
    }

    public static void main(String[] args) {
        CalendarServer.getServer();
    }
}
