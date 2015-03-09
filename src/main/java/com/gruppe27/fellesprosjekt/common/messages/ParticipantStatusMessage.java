package com.gruppe27.fellesprosjekt.common.messages;

public class ParticipantStatusMessage {

    public enum Command {
        CHANGE_STATUS
    }
    public enum Status {
        ATTENDING,
        NOT_ATTENDING,
        MAYBE
    }

    Status status;
    Command command;
    int eventId;

    public ParticipantStatusMessage() {
    }

    public ParticipantStatusMessage(Command command, Status status, int eventId) {
        this.command = command;
        this.status = status;
        this.eventId = eventId;
    }

    public Command getCommand() {
        return command;
    }

    public Status getStatus() {
        return status;
    }

    public int getEventId() { return eventId; }
}

