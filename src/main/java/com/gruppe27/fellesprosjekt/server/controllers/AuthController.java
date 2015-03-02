package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.AuthCompleteMessage;
import com.gruppe27.fellesprosjekt.common.messages.AuthMessage;
import com.gruppe27.fellesprosjekt.common.messages.ErrorMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;
import com.gruppe27.fellesprosjekt.server.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {
    private static AuthController instance = null;

    protected AuthController() {}

    public static AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }

    public void handleMessage(CalendarConnection connection, Object message) {
        AuthMessage authMessage = (AuthMessage) message;
        switch (authMessage.getCommand()) {
            case LOGIN:
                login(connection, authMessage);
                break;
        }
    }

    private void login(CalendarConnection connection, AuthMessage authMessage) {
        try {
            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                    "SELECT username, name, password FROM User WHERE username=? AND password=?"
            );

            statement.setString(1, authMessage.getUsername());
            statement.setString(2, authMessage.getPassword());
            ResultSet result = statement.executeQuery();

            AuthCompleteMessage authCompleteMessage;
            if (result.next()) {
                String username = result.getString("username");
                String name = result.getString("name");

                User user = new User(username, name);
                connection.setUser(user);
                authCompleteMessage = new AuthCompleteMessage(AuthCompleteMessage.Command.SUCCESSFUL_LOGIN, user);
                System.out.println("bra auth sender " + authCompleteMessage.getUser());
                connection.sendTCP(authCompleteMessage);
            } else {
                System.out.println("fant ingen bruker");
                authCompleteMessage = new AuthCompleteMessage(AuthCompleteMessage.Command.UNSUCCESSFUL_LOGIN);
                connection.sendTCP(authCompleteMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }
}
