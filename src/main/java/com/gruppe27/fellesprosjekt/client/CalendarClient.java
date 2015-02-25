package com.gruppe27.fellesprosjekt.client;

import com.esotericsoftware.kryonet.Client;
import com.gruppe27.fellesprosjekt.common.Network;

import java.io.IOException;

public class CalendarClient {
    public static final int TIMEOUT = 5000;

    Client client;

    public CalendarClient() {
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

    public void sendMessage(Object message) {
        client.sendTCP(message);
    }
}
