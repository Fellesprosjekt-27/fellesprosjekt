package com.gruppe27.fellesprosjekt.server;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.gruppe27.fellesprosjekt.common.messages.AuthMessage;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.server.controllers.AuthController;
import com.gruppe27.fellesprosjekt.server.controllers.EventController;


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

    }

    public void connected(com.esotericsoftware.kryonet.Connection c) {
        CalendarConnection connection = (CalendarConnection) c;
        System.out.println("User connected.");
    }
}

