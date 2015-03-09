package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Notification;

public class NotificationMessage {
    public enum Command {
        RECEIVE_NOTIFICATION
    }

    Notification notification;
    Command command;

    public NotificationMessage() {
    }

    public NotificationMessage(Command command, Notification notification) {
        this.command = command;
        this.notification = notification;
    }

    public Command getCommand() {
        return command;
    }

    public Notification getNotification() {
        return notification;
    }
}

