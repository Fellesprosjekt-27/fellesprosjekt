package com.gruppe27.fellesprosjekt.server;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.gruppe27.fellesprosjekt.common.messages.*;
import com.gruppe27.fellesprosjekt.server.controllers.*;


public class CalendarListener extends Listener {
    Server server;

    public CalendarListener(Server server) {
        this.server = server;
    }

    public void received(com.esotericsoftware.kryonet.Connection c, Object message) {
        CalendarConnection connection = (CalendarConnection) c;

        if (message instanceof AuthMessage) {
            AuthController.getInstance().handleMessage(connection, message);
            return;
        }

        if (message instanceof EventMessage) {
            EventController.getInstance().handleMessage(connection, message);
            return;
        }
        if (message instanceof RoomMessage) {
            RoomController.getInstance().handleMessage(connection,message);
        }

        if (message instanceof RoomRequestMessage) {
            RoomRequestController.getInstance().handleMessage(connection, message);
            return;
        }

        if (message instanceof UserMessage) {
            UserController.getInstance().handleMessage(connection, message);
            return;
        }

        if (message instanceof ParticipantStatusMessage) {
            ParticipantStatusController.getInstance().handleMessage(connection, message);
            return;
        }

    }

    public void connected(com.esotericsoftware.kryonet.Connection c) {
        CalendarConnection connection = (CalendarConnection) c;
        System.out.println("User connected.");
    }
}

