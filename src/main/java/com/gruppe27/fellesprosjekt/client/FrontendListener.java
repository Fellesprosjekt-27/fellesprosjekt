package com.gruppe27.fellesprosjekt.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.common.messages.GeneralMessage;

public class FrontendListener extends Listener {
    private Client client;

    public FrontendListener(Client client) {
        this.client = client;
    }

    public void connected(Connection connection) {
        System.out.println("Koblet til server.");
    }

    public void received(Connection connection, Object object) {
        if (object instanceof GeneralMessage) {
            GeneralMessage generalMessage = (GeneralMessage) object;
            System.out.println(generalMessage.getMessage());
            return;
        }
    }

    public void disconnected(Connection connection) {
        System.out.println("disconnected");
        System.exit(0);
    }
}
