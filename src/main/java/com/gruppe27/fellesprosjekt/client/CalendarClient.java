package com.gruppe27.fellesprosjekt.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.common.Network;
import com.gruppe27.fellesprosjekt.common.TestMessage;

import java.io.IOException;

public class CalendarClient {
    public static final int TIMEOUT = 5000;

    Client client;

    public CalendarClient() {
        client = new Client();
        client.start();

        Network.register(client);

        client.addListener(new Listener() {
            public void connected(Connection connection) {
                TestMessage testMessage = new TestMessage("Nå kom jeg inn :)");
                client.sendTCP(testMessage);
            }

            public void received(Connection connection, Object object) {
                if (object instanceof TestMessage) {
                    TestMessage testMessage = (TestMessage) object;
                    System.out.println("I got a message: " + testMessage.getMessage());
                }
            }

            public void disconnected(Connection connection) {
                System.out.println("disconnected");
                System.exit(0);
            }
        });

        try {
            client.connect(TIMEOUT, "", Network.PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void sendMessage(String message) {
        TestMessage testMessage = new TestMessage(message);
        client.sendTCP(testMessage);
    }
}
