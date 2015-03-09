package com.gruppe27.fellesprosjekt.client.events;

import com.gruppe27.fellesprosjekt.client.components.MonthEventSquare;
import com.gruppe27.fellesprosjekt.common.Event;
import javafx.event.EventType;

public class EventBoxClicked extends javafx.event.Event {
    public static final EventType eventType = new EventType(ANY, "EventBoxClicked");
    private Event event;
    private MonthEventSquare square;

    public EventBoxClicked(Event event, MonthEventSquare square) {
        super(eventType);
        this.event = event;
        this.square = square;
    }

    public Event getEvent() {
        return event;
    }

    public MonthEventSquare getSquare() {
        return square;
    }
}
