package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.AuthMessage;
import com.gruppe27.fellesprosjekt.common.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.GeneralMessage;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.UserMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class AuthController extends Controller {

    public AuthController(Connection databaseConnection) {
        super(databaseConnection);
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
            PreparedStatement statement = databaseConnection.prepareStatement(
                    "SELECT username, name, password FROM User WHERE username=? AND password=?"
            );

            statement.setString(1, authMessage.getUsername());
            statement.setString(2, authMessage.getPassword());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String username = result.getString("username");
                String name = result.getString("name");

                User user = new User(username, name);
                UserMessage userMessage = new UserMessage(UserMessage.Command.SUCCESSFUL_LOGIN, user);
                connection.sendTCP(userMessage);
            } else {
                System.out.println("fant ingen bruker");
                GeneralMessage badAuthMessage = new GeneralMessage(GeneralMessage.Command.UNSUCCESSFUL_LOGIN,
                        "Ugyldig brukernavn og/eller passord.");
                connection.sendTCP(badAuthMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }
}
