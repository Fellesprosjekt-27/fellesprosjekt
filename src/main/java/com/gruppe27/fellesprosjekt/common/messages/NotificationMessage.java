package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Notification;

import java.util.ArrayList;

public class NotificationMessage {
    public enum Command {
        GETALL_NOTIFICATIONS,
        RECEIVE_NOTIFICATION
    }

    ArrayList<Notification> notifications;
    Command command;

    public NotificationMessage() {
    }

    public NotificationMessage(Command command) {
        this.command = command;
        this.notifications = new ArrayList<>();
    }

    public NotificationMessage(Command command, Notification notification) {
        this.command = command;
        this.notifications = new ArrayList<>();
        this.notifications.add(notification);
    }

    public Command getCommand() {
        return command;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }
}

