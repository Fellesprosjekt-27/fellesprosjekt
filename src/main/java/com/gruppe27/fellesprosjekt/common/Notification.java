package com.gruppe27.fellesprosjekt.common;

import java.time.LocalDateTime;

public class Notification {

    String username;
    Event event;
    String message;
    LocalDateTime timestamp;

    public Notification() {
    }

    public Notification(Event event, String message, String username) {
        this.event = event;
        this.message = message;
        this.username = username;
    }

    public Event getEvent() {
        return event;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }
}
