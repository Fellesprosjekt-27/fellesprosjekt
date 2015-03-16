package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Event;

public class ParticipantStatusMessage {
    public enum Command {
        CHANGE_STATUS
    }

    Command command;
    Event event;

    public ParticipantStatusMessage() {
    }

    public ParticipantStatusMessage(Command command, Event event) {
        this.command = command;
        this.event = event;
    }

    public Command getCommand() {
        return command;
    }

    public Event getEvent() {
        return event;
    }
}

