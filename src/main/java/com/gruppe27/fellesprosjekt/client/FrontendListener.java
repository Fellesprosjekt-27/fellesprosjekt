package com.gruppe27.fellesprosjekt.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.common.messages.GeneralMessage;
import com.gruppe27.fellesprosjekt.common.messages.TestMessage;
import com.gruppe27.fellesprosjekt.common.messages.UserMessage;

public class FrontendListener extends Listener {
    private Client client;

    public FrontendListener(Client client) {
        this.client = client;
    }

    public void connected(Connection connection) {
        TestMessage testMessage = new TestMessage("Nå kom jeg inn :)");
        client.sendTCP(testMessage);
    }

    public void received(Connection connection, Object object) {
        if (object instanceof GeneralMessage) {
            GeneralMessage generalMessage = (GeneralMessage) object;
            System.out.println(generalMessage.getMessage());
            return;
        }

        if (object instanceof UserMessage) {
            // TODO: Send this to the user controller
            UserMessage userMessage = (UserMessage) object;
            System.out.println("User " + userMessage.getUser().toString());
            return;
        }

        if (object instanceof TestMessage) {
            TestMessage testMessage = (TestMessage) object;
            System.out.println("I got a message: " + testMessage.getMessage());
            return;
        }
    }

    public void disconnected(Connection connection) {
        System.out.println("disconnected");
        System.exit(0);
    }
}
