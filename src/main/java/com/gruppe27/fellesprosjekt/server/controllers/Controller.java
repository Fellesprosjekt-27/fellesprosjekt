package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.server.CalendarConnection;

import java.sql.Connection;

public abstract class Controller {
    protected Connection databaseConnection;

    public Controller(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public abstract void handleMessage(CalendarConnection connection, Object message);
}
