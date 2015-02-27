package com.gruppe27.fellesprosjekt.common.messages;

public class GeneralMessage {
    public enum Command {
        SUCCESSFUL_CREATE
    }

    String message;
    Command command;

    public GeneralMessage() {}

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
}
