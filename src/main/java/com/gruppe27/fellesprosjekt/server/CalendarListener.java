package com.gruppe27.fellesprosjekt.server;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.gruppe27.fellesprosjekt.common.messages.AuthMessage;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.common.messages.TestMessage;
import com.gruppe27.fellesprosjekt.server.controllers.AuthController;
import com.gruppe27.fellesprosjekt.server.controllers.EventController;

import java.sql.Connection;

public class CalendarListener extends Listener {
    Server server;
    Connection databaseConnection;

    AuthController authController;
    EventController eventController;

    public CalendarListener(Server server, Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
        this.server = server;
        this.initializeControllers();
    }

    private void initializeControllers() {
        authController = new AuthController(databaseConnection);
    }

    public void received(com.esotericsoftware.kryonet.Connection c, Object message) {
        CalendarConnection connection = (CalendarConnection) c;

        if (message instanceof AuthMessage) {
            authController.handleMessage(connection, message);
            return;
        }

        if (message instanceof TestMessage) {
            System.out.println("Received a testmessage");
            TestMessage received = (TestMessage) message;
            TestMessage newMessage = new TestMessage("Received message: " + received.getMessage());

            server.sendToAllTCP(newMessage);
            return;
        }

        if(message instanceof EventMessage) {
            eventController.handleMessage(message);
            return;
        }
    }

    public void connected(Connection c) {
        CalendarConnection connection = (CalendarConnection) c;
        TestMessage sendMessage = new TestMessage("Hi!");
        connection.sendTCP(sendMessage);
    }
}

