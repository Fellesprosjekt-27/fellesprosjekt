package com.gruppe27.fellesprosjekt.common;

import java.time.LocalDateTime;

public class Notification {
    Event event;
    String message;
    String username;
    LocalDateTime timestamp;
    NotificationType type;

    public enum NotificationType {
        INVITATION, PARTICIPATION_DECLINED, EVENT_CHANGED, CONFLICTING_EVENTS
    }

    public Notification() {
    }

    public Notification(String username, Event event, String message, LocalDateTime timestamp, NotificationType type) {
        this.username = username;
        this.event = event;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Notification(String username, Event event, String message, NotificationType type) {
        this.username = username;
        this.event = event;
        this.message = message;
        this.type = type;
    }

    public NotificationType getType() {
        return type;
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

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }
}