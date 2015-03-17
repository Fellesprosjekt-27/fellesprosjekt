package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.User;

import java.time.LocalDate;
import java.util.HashSet;

public class EventMessage {


    HashSet<Event> events;
    LocalDate from;
    LocalDate to;
    Event event;
    Command command;
    User user;

    public enum Command {
        CREATE_EVENT,
        RECEIVE_EVENTS,
        SEND_EVENTS
    }

    public EventMessage() {
    }

    public EventMessage(Command command, Event event) {
        this.command = command;
        this.event = event;
        this.events = null;
        this.from = null;
        this.to = null;
    }

    public EventMessage(User user, Command command, LocalDate from, LocalDate to) {
        this.user = user;
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

    public User getUser() {
        return user;
    }
}
