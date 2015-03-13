package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Event;

public class ParticipantStatusMessage {

    Event.Status status;
    Command command;
    int eventId;
    public ParticipantStatusMessage() {
    }

    public ParticipantStatusMessage(Command command, Event.Status status, int eventId) {
        this.command = command;
        this.status = status;
        this.eventId = eventId;
    }

    public Command getCommand() {
        return command;
    }

    public Event.Status getStatus() {
        return status;
    }

    public int getEventId() {
        return eventId;
    }

    public enum Command {
        CHANGE_STATUS
    }
}

