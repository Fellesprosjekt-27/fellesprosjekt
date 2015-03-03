package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Event;

import java.util.HashSet;

public class EventMessage {


    public enum Command {
        CREATE_EVENT,
        SEND_ALL,
        RECIEVE_ALL,
    }

    HashSet<Event> events;
    Event event;
    Command command;

    public EventMessage() {
    }

    public EventMessage(Command command, Event event) {
        this.command = command;
        this.event = event;
        this.events = null;
    }

    public EventMessage(Command command, HashSet<Event> events) {
        this.command = command;
        this.events = events;
        this.event = null;
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

}
