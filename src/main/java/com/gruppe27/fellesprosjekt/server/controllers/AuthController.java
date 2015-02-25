package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.AuthMessage;
import com.gruppe27.fellesprosjekt.common.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.GeneralMessage;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.UserMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    private void login(AuthMessage authMessage) {
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
                calendarConnection.sendTCP(userMessage);
            } else {
                System.out.println("fant ingen bruker");
                GeneralMessage badAuthMessage = new GeneralMessage(GeneralMessage.Command.UNSUCCESSFUL_LOGIN,
                        "Ugyldig brukernavn og/eller passord.");
                calendarConnection.sendTCP(badAuthMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            calendarConnection.sendTCP(error);
        }
    }
}
