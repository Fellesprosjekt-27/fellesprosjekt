package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.server.CalendarConnection;

public abstract class Controller {
    protected CalendarConnection connection;

    public Controller(CalendarConnection connection) {
        this.connection = connection;
    }

    public abstract void handleMessage(Object message);
}
