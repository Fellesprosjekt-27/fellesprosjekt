package com.gruppe27.fellesprosjekt.common;

public class Notification {

    Event event;
    String message;

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
}
