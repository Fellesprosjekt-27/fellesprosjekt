package com.gruppe27.fellesprosjekt.client.events;

import com.gruppe27.fellesprosjekt.common.Event;
import javafx.event.EventType;

import java.util.ArrayList;

public class EventBoxClicked extends javafx.event.Event {
    public static final EventType eventType = new EventType(ANY, "EventBoxClicked");
    private final ArrayList<Event> conflictingEvents;
    private Event event;
    private double screenX;
    private double screenY;

    public EventBoxClicked(Event event, double screenX, double screenY, ArrayList<Event> conflictingEvents) {
        super(eventType);
        this.event = event;
        this.screenX = screenX;
        this.screenY = screenY;
        this.conflictingEvents = conflictingEvents;
    }

    public Event getEvent() {
        return event;
    }

    public double getScreenX() {
        return screenX;
    }

    public double getScreenY() {
        return screenY;
    }

    public ArrayList<Event> getConflictingEvents() {
        return conflictingEvents;
    }
}
