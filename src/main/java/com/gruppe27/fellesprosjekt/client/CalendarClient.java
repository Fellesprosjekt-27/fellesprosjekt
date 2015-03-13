package com.gruppe27.fellesprosjekt.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.common.Network;

import java.io.IOException;

public class CalendarClient {
    public static final int TIMEOUT = 5000;
    private static CalendarClient instance;
    private Client client;

    protected CalendarClient() {
        client = new Client();
        client.start();

        Network.register(client);

        client.addListener(new FrontendListener(client));

        try {
            client.connect(TIMEOUT, "", Network.PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static CalendarClient getInstance() {
        if (instance == null) {
            instance = new CalendarClient();
        }
        return instance;
    }

    public void addListener(Listener listener) {
        client.addListener(listener);
    }

    public void removeListener(Listener listener) {
        client.removeListener(listener);
    }

    public void sendMessage(Object message) {
        client.sendTCP(message);
    }
}
