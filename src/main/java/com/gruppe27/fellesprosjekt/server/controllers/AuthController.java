package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.AuthMessage;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;

public class AuthController extends Controller {

    public AuthController(CalendarConnection connection) {
        super(connection);
    }

    public void handleMessage(Object message) {
        AuthMessage authMessage = (AuthMessage) message;
        switch (authMessage.getCommand()) {
            case LOGIN:
                login(authMessage);
                break;
        }
    }

    private User login(AuthMessage authMessage) {
        return null;
    }
}
