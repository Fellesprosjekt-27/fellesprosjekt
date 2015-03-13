package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Event;

public class InviteMessage {

    Event event;
    Command command;
    public InviteMessage() {
    }

    public InviteMessage(Command command, Event event) {
        this.command = command;
        this.event = event;
    }

    public Command getCommand() {
        return command;
    }

    public Event getEvent() {
        return event;
    }

    public enum Command {

    }

}
