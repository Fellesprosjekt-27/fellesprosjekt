package com.gruppe27.fellesprosjekt.common;

import java.time.LocalDateTime;

public class Notification {

    Event event;
    String message;
    LocalDateTime timestamp;

    public Notification() {
    }

    public Notification(Event event, String message) {
        this.event = event;
        this.message = message;
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
}
