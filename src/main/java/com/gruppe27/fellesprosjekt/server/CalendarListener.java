package com.gruppe27.fellesprosjekt.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.gruppe27.fellesprosjekt.common.AuthMessage;
import com.gruppe27.fellesprosjekt.common.TestMessage;
import com.gruppe27.fellesprosjekt.server.controllers.AuthController;

public class CalendarListener extends Listener {
    Server server;
    CalendarConnection connection;

    AuthController authController;

    public CalendarListener(Server server, CalendarConnection connection) {
        this.server = server;
        this.connection = connection;
        this.initializeControllers();
    }

    private void initializeControllers() {
        authController = new AuthController(connection);
    }

    public void received(Connection c, Object message) {
        if (message instanceof AuthMessage) {
            authController.handleMessage(message);
            return;
        }

        if (message instanceof TestMessage) {
            System.out.println("Received a testmessage");
            TestMessage received = (TestMessage) message;
            TestMessage newMessage = new TestMessage("Received message: " + received.getMessage());

            server.sendToAllTCP(newMessage);
            return;
        }
    }

    public void connected(Connection c) {
        CalendarConnection connection = (CalendarConnection) c;
        TestMessage sendMessage = new TestMessage("Hi!");
        connection.sendTCP(sendMessage);
    }
}

