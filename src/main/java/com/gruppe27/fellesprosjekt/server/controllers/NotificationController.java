package com.gruppe27.fellesprosjekt.server.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.Notification;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.NotificationMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;
import com.gruppe27.fellesprosjekt.server.CalendarServer;
import com.gruppe27.fellesprosjekt.server.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationController {
    private static NotificationController instance = null;

    protected NotificationController() {
    }

    public static NotificationController getInstance() {
        if (instance == null) {
            instance = new NotificationController();
        }
        return instance;
    }

    private CalendarConnection findConnectedUser(User user) {
        for (Connection connection : CalendarServer.getServer().getConnections()) {
            CalendarConnection calendarConnection = (CalendarConnection) connection;
            if (calendarConnection.getUser().getUsername().equals(user.getUsername())) {
                return calendarConnection;
            }
        }

        return null;
    }

    private void createNotification(Notification notification) throws SQLException {
        PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                "INSERT INTO Notification(event_id, user_username, message) VALUES (?,?,?)"
        );

        statement.setInt(1, notification.getEvent().getId());
        statement.setString(2, notification.getUsername());
        statement.setString(3, notification.getMessage());
        statement.executeUpdate();
    }

    public void newEventNotification(Event event, User user) {
        String message = "You have been invited to join: " + event.getName();
        Notification notification = new Notification(user.getUsername(), event, message,
                Notification.NotificationType.INVITATION);

        try {
            createNotification(notification);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        CalendarConnection connection = findConnectedUser(user);
        if (connection != null) {
            NotificationMessage notificationMessage = new NotificationMessage(
                    NotificationMessage.Command.RECEIVE_NOTIFICATION,
                    notification
            );

            connection.sendTCP(notificationMessage);
        }
    }
}
