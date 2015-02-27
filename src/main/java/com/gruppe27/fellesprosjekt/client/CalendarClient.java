package com.gruppe27.fellesprosjekt.client;

import com.esotericsoftware.kryonet.Client;
import com.gruppe27.fellesprosjekt.common.Network;

import java.io.IOException;

public class CalendarClient {
    private static CalendarClient instance;

    public static final int TIMEOUT = 5000;

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

    public void sendMessage(Object message) {
        client.sendTCP(message);
    }
}
