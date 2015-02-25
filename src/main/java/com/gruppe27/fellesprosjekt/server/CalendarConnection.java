package com.gruppe27.fellesprosjekt.server;

import com.esotericsoftware.kryonet.Connection;
import com.gruppe27.fellesprosjekt.common.User;

public class CalendarConnection extends Connection {
    private User user;
    private java.sql.Connection databaseConnection;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public java.sql.Connection getDatabaseConnection() {
        return databaseConnection;
    }

    public void setDatabaseConnection(java.sql.Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
}
