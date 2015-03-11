package com.gruppe27.fellesprosjekt.server;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.gruppe27.fellesprosjekt.common.messages.AuthMessage;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.common.messages.UserMessage;
import com.gruppe27.fellesprosjekt.server.controllers.AuthController;
import com.gruppe27.fellesprosjekt.server.controllers.EventController;
import com.gruppe27.fellesprosjekt.server.controllers.UserController;
import com.gruppe27.fellesprosjekt.common.messages.RoomMessage;
import com.gruppe27.fellesprosjekt.server.controllers.RoomController;
import com.gruppe27.fellesprosjekt.server.controllers.RequestController;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;


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
            return;
        }

        if (message instanceof RequestMessage) {
            RequestController.getInstance().handleMessage(connection, message);
            return;
        }

        if (message instanceof UserMessage) {
            UserController.getInstance().handleMessage(connection, message);
        }

    }

    public void connected(com.esotericsoftware.kryonet.Connection c) {
        CalendarConnection connection = (CalendarConnection) c;
        System.out.println("User connected.");
    }
}

