package com.gruppe27.fellesprosjekt.server.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.Notification;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.messages.NotificationMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;
import com.gruppe27.fellesprosjekt.server.CalendarServer;
import com.gruppe27.fellesprosjekt.server.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class NotificationController {
    private static final String QUERY = "SELECT Notification.timestamp, Notification.user_username, " +
            "Notification.type, Notification.message, Event.name AS `event.name`, Event.id AS `event.id`, " +
            "Event.date AS `event.date`, " +
            "Event.start AS `event.start`, Event.end AS `event.end`, " +
            "Event.room  AS `event.room`, Creator.username AS `creator.username`, Creator.name AS `creator.name` " +
            "from Notification INNER JOIN Event ON Event.id=Notification.event_id " +
            "INNER JOIN User as Creator ON Creator.username=Event.creator " +
            "WHERE user_username=? ORDER BY Notification.timestamp DESC";
    private static NotificationController instance = null;

    protected NotificationController() {
    }

    public static NotificationController getInstance() {
        if (instance == null) {
            instance = new NotificationController();
        }
        return instance;
    }

    public void handleMessage(CalendarConnection connection, Object message) {
        NotificationMessage notificationMessage = (NotificationMessage) message;
        switch (notificationMessage.getCommand()) {
            case GETALL_NOTIFICATIONS:
                sendAllNotifications(connection);
                break;
        }
    }

    private ArrayList<Notification> parseResult(ResultSet resultSet) throws SQLException {
        ArrayList<Notification> notifications = new ArrayList<>();
        while (resultSet.next()) {
            User creator = new User(resultSet.getString("creator.username"), resultSet.getString("creator.name"));
            Event event = new Event(
                    resultSet.getString("event.name"),
                    creator,
                    resultSet.getDate("event.date").toLocalDate(),
                    resultSet.getTime("event.start").toLocalTime(),
                    resultSet.getTime("event.end").toLocalTime(),
                    resultSet.getInt("event.capacity_need")
            );
            Notification notification = new Notification(
                    resultSet.getString("user_username"),
                    event,
                    resultSet.getString("message"),
                    resultSet.getTimestamp("timestamp").toLocalDateTime(),
                    Notification.NotificationType.valueOf(resultSet.getString("type"))
            );
            notifications.add(notification);
        }

        return notifications;
    }

    private void sendAllNotifications(CalendarConnection connection) {
        try {
            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(QUERY);
            statement.setString(1, connection.getUser().getUsername());

            ResultSet resultSet = statement.executeQuery();
            ArrayList<Notification> notifications = parseResult(resultSet);
            NotificationMessage createdMessage = new NotificationMessage(
                    NotificationMessage.Command.RECEIVE_NOTIFICATION,
                    notifications
            );

            connection.sendTCP(createdMessage);
            System.out.println("sent " + notifications.size() + "events.");
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
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
                "INSERT INTO Notification(event_id, user_username, message, type, timestamp) " +
                        "VALUES (?, ?, ?, ?, ?)"
        );

        statement.setInt(1, notification.getEvent().getId());
        statement.setString(2, notification.getUsername());
        statement.setString(3, notification.getMessage());
        statement.setString(4, notification.getType().toString());
        statement.setString(5, notification.getTimestamp().toString());
        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Couldn't create notification");
        }
    }

    public void newEventNotification(Event event, User user) {
        String message = "You have been invited to join: " + event.getName();
        Notification notification = new Notification(user.getUsername(), event, message,
                LocalDateTime.now(),
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
