package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.server.CalendarConnection;

public abstract class Controller {
    protected CalendarConnection calendarConnection;
    protected java.sql.Connection databaseConnection;

    public Controller(CalendarConnection calendarConnection) {
        this.calendarConnection = calendarConnection;
        this.databaseConnection = calendarConnection.getDatabaseConnection();
    }

    public abstract void handleMessage(Object message);
}
