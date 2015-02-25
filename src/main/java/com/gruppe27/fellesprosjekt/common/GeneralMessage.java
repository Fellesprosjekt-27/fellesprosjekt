package com.gruppe27.fellesprosjekt.common;

public class GeneralMessage {
    public enum Command {
        UNSUCCESSFUL_LOGIN
    }

    private String message;
    private Command command;

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
