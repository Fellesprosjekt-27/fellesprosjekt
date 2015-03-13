package com.gruppe27.fellesprosjekt.common.messages;

public class GeneralMessage {
    String message;
    Command command;
    public GeneralMessage() {
    }

    public GeneralMessage(Command command, String message) {
        this.command = command;
        this.message = message;
    }

    public Command getCommand() {
        return command;
    }

    public String getMessage() {
        return message;
    }

    public enum Command {
        SUCCESSFUL_CREATE
    }
}
