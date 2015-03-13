package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Event;

import java.time.LocalDate;
import java.util.HashSet;

public class EventMessage {


    HashSet<Event> events;
    LocalDate from;
    LocalDate to;
    Event event;
    Command command;
    public EventMessage() {
    }

    public EventMessage(Command command, Event event) {
        this.command = command;
        this.event = event;
        this.events = null;
        this.from = null;
        this.to = null;
    }

    public EventMessage(Command command, LocalDate from, LocalDate to) {
        this.command = command;
        this.event = null;
        this.events = null;
        this.from = from;
        this.to = to;
    }

    public EventMessage(Command command, HashSet<Event> events) {
        this.command = command;
        this.events = events;
        this.event = null;
        this.from = null;
        this.to = null;
    }

    public Command getCommand() {
        return command;
    }

    public Event getEvent() {
        return event;
    }

    public HashSet<Event> getEvents() {
        return events;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public enum Command {
        CREATE_EVENT,
        RECEIVE_EVENTS,
        SEND_EVENTS
    }
}
