package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.messages.UserMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;
import com.gruppe27.fellesprosjekt.server.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class UserController {

    private static UserController instance = null;

    protected UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public void handleMessage(CalendarConnection connection, Object message) {
        UserMessage userMessage = (UserMessage) message;
        switch (userMessage.getCommand()) {
            case SEND_ALL:
                sendAllUsers(connection);
                break;
            case RECEIVE_ALL:
                break;
        }
    }

    private void sendAllUsers(CalendarConnection connection) {
        try {
            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                    "SELECT username, name FROM User");
            ResultSet resultSet = statement.executeQuery();
            HashSet<User> users = parseUserResult(resultSet);
            UserMessage createdMessage = new UserMessage(UserMessage.Command.RECEIVE_ALL, users);
            connection.sendTCP(createdMessage);
            System.out.println("sent " + users.size() + " users.");
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }

    private HashSet<User> parseUserResult(ResultSet resultSet) throws SQLException {
        HashSet<User> users = new HashSet<>();
        while (resultSet.next()) {
            User user = new User(resultSet.getString("name"), resultSet.getString("username"));
            users.add(user);
        }
        return users;
    }


}
